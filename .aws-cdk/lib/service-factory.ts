import * as cdk from '@aws-cdk/core';
import * as ec2 from "@aws-cdk/aws-ec2";
import * as ecs from "@aws-cdk/aws-ecs";
import {Secret} from "@aws-cdk/aws-ecs";
import * as ecr from "@aws-cdk/aws-ecr";
import * as s3 from '@aws-cdk/aws-s3';
import {ApplicationLoadBalancedFargateService} from "@aws-cdk/aws-ecs-patterns";
import * as acm from '@aws-cdk/aws-certificatemanager';
import * as route53 from "@aws-cdk/aws-route53";
import {RepositoryName} from "./repository-name";
import {ddApiKey, deployEnv, envSpecificName, githubSha, imageTag, secureSsmParameterForEnv,} from "./utils";
import {paramCase} from "param-case";
import {headerCase} from "header-case";
import {addDataDogContainer, defaultDatadogServiceEnv} from "./data-dog-container";
import {RetentionDays} from "@aws-cdk/aws-logs";
import {ApplicationProtocol, ApplicationProtocolVersion, SslPolicy} from "@aws-cdk/aws-elasticloadbalancingv2";
import {addEventStoreContainer} from "./event-store-container";
import {Duration} from "@aws-cdk/core";

export class Service extends cdk.Stack {

    static from(scope: cdk.Construct, template: ServiceTemplate): Service {
        const stack = new cdk.Stack(scope, paramCase(envSpecificName(template.name)), template.stackProps)
        const hostedZone = route53.HostedZone.fromLookup(stack, "psinder-hosted-zone", {
            domainName: 'psinder.link'
        })
        const repository = ecr.Repository.fromRepositoryName(stack, headerCase(`${template.name}-repository`), template.repository)
        const certificate = acm.Certificate.fromCertificateArn(stack, 'Certificate', 'arn:aws:acm:eu-north-1:993160204208:certificate/05e1a3fd-aed9-49ae-9f2e-8699d3b10449')
        const taskDefinition = new ecs.FargateTaskDefinition(stack, headerCase(`${template.name}-task-definition`), {
            cpu: 1024,
            memoryLimitMiB: 2048
        })

        const securityGroups = template.securityGroups
            ?.map((securityGroup) => {
                const group = new ec2.SecurityGroup(stack, securityGroup.id, securityGroup.props)
                group.addIngressRule(securityGroup.ingressProps.peer, securityGroup.ingressProps.port)
                return group
            })

        const secrets = template.secrets ?
            Object.assign({}, ...Object.entries(template.secrets).map(([k, v]) => ({[k]: Secret.fromSsmParameter(secureSsmParameterForEnv(stack, v))})))
            : {};

        taskDefinition.addContainer(headerCase(`${template.name}-container`), {
            image: ecs.ContainerImage.fromEcrRepository(repository, imageTag()),
            portMappings: template.port?.map((port) => {
                return {
                    containerPort: port,
                    hostPort: port,
                    protocol: ecs.Protocol.TCP
                }
            }),
            healthCheck: {
                command: ["curl", "--fail", "http://localhost:8080/v1/application-status/health"],
                startPeriod: Duration.seconds(200)
            },
            secrets: secrets,
            environment: template.configureDatadog ? Object.assign({}, template.environment, defaultDatadogServiceEnv(template.name)) : template.environment,
            logging: template.configureDatadog ? datadogLogging(template.name) : defaultLogging(),
            dockerLabels: template.configureDatadog ? datadogDockerLabels(template.name) : undefined,
        })
        if (template.configureDatadog) {
            addDataDogContainer(taskDefinition)
        }

        addEventStoreContainer(taskDefinition) //temp solution because cloud version isn't so cheap for testing purposes

        const service = new ApplicationLoadBalancedFargateService(stack, headerCase(`${template.name}-service`), {
            cluster: template.cluster,
            taskDefinition: taskDefinition,
            desiredCount: 1,
            domainName: 'psinder.link',
            domainZone: hostedZone,
            securityGroups: securityGroups,
            assignPublicIp: true,
            loadBalancerName: 'psinder',
            targetProtocol: ApplicationProtocol.HTTPS,
            certificate: certificate,
            redirectHTTP: true,
            protocol: ApplicationProtocol.HTTPS,
            openListener: true,
            sslPolicy: SslPolicy.RECOMMENDED,
            protocolVersion: ApplicationProtocolVersion.HTTP2,
            publicLoadBalancer: true,
        })

        // @ts-ignore
        service.taskDefinition.defaultContainer!.props.healthCheck = <ecs.HealthCheck>{
            command: ["curl", "--fail", "http://localhost:8080/v1/application-status/health"],
            interval: cdk.Duration.seconds(15),
            retries: 3,
            timeout: cdk.Duration.seconds(5),
        };

        template.s3Buckets?.map((bucketName) => {
            const bucketEnvSpecificName = envSpecificName(bucketName)

            const bucket = s3.Bucket.fromBucketName(stack, `${bucketEnvSpecificName}-bucket`, bucketEnvSpecificName)

            bucket.grantReadWrite(service.taskDefinition.taskRole)
        })

        return stack
    }
}

export interface ServiceTemplate {
    readonly vpc: ec2.IVpc,
    readonly cluster: ecs.ICluster,
    readonly cpu?: string,
    readonly memoryMiB?: string,
    readonly name: string,
    readonly repository: RepositoryName,
    readonly port?: number[],
    readonly securityGroupName?: string,
    readonly securityGroups?: SecurityGroupIdAndProps[],
    readonly environment?: {
        [key: string]: string;
    },
    readonly secrets?: {
        [key: string]: string;
    },
    readonly configureDatadog: boolean,
    readonly assignPublicIp?: boolean,
    readonly stackProps: cdk.StackProps,
    readonly s3Buckets?: string[]
}

export interface SecurityGroupIdAndProps {
    readonly id: string,
    readonly props: ec2.SecurityGroupProps,
    readonly ingressProps: {
        peer: ec2.IPeer,
        port: ec2.Port
    }
}

function datadogLogging(serviceName: string): ecs.LogDriver {
    return ecs.LogDrivers.firelens({
        options: {
            'dd_message_key': 'log',
            'apikey': ddApiKey(),
            'provider': 'ecs',
            'dd_tags': `env:${deployEnv()},version:${githubSha()}`,
            'dd_service': serviceName,
            'dd_env': deployEnv(),
            'dd_version': githubSha(),
            'dd_source': 'java',
            'Host': 'http-intake.logs.datadoghq.com',
            'TLS': 'on',
            'Name': 'datadog'
        }
    })
}

function defaultLogging(retention?: RetentionDays): ecs.LogDriver {
    return ecs.LogDrivers.awsLogs({
        streamPrefix: 'ecs',
        logRetention: retention || RetentionDays.ONE_DAY
    })
}

function datadogDockerLabels(serviceName: string): { [key: string]: string } {
    return {
        'com.datadoghq.tags.env': deployEnv(),
        'com.datadoghq.tags.service': serviceName,
        'com.datadoghq.tags.version': githubSha()
    }
}