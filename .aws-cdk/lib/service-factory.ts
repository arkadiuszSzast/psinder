import * as cdk from '@aws-cdk/core';
import {Duration} from '@aws-cdk/core';
import * as ec2 from "@aws-cdk/aws-ec2";
import * as ecs from "@aws-cdk/aws-ecs";
import {Secret} from "@aws-cdk/aws-ecs";
import * as ecr from "@aws-cdk/aws-ecr";
import * as s3 from '@aws-cdk/aws-s3';
import * as acm from '@aws-cdk/aws-certificatemanager';
import * as route53 from "@aws-cdk/aws-route53";
import * as route53_targets from "@aws-cdk/aws-route53-targets";
import {RepositoryName} from "./repository-name";
import {ddApiKey, deployEnv, envSpecificName, githubSha, imageTag, secureSsmParameterForEnv,} from "./utils";
import {paramCase} from "param-case";
import {headerCase} from "header-case";
import {addDataDogContainer, defaultDatadogServiceEnv} from "./data-dog-container";
import {RetentionDays} from "@aws-cdk/aws-logs";
import {
    ApplicationLoadBalancer,
    ApplicationProtocol,
    ApplicationProtocolVersion,
    IpAddressType,
    Protocol
} from "@aws-cdk/aws-elasticloadbalancingv2";
import {addEventStoreContainer} from "./event-store-container";

export class Service extends cdk.Stack {

    static from(scope: cdk.Construct, template: ServiceTemplate): Service {
        const stack = new cdk.Stack(scope, paramCase(envSpecificName(template.name)), template.stackProps)
        const hostedZone = route53.HostedZone.fromLookup(stack, "psinder-hosted-zone", {
            domainName: 'psinder.link'
        })
        const repository = ecr.Repository.fromRepositoryName(stack, headerCase(`${template.name}-repository`), template.repository)
        const certificate = new acm.Certificate(stack, 'Certificate', {
            domainName: '*.psinder.link',
            validation: acm.CertificateValidation.fromDns(hostedZone)
        });
        const taskDefinition = new ecs.FargateTaskDefinition(stack, headerCase(`${template.name}-task-definition`), {
            cpu: 1024,
            memoryLimitMiB: 2048,
        })

        const securityGroups = template.securityGroups
            ?.map((securityGroup) => {
                const group = new ec2.SecurityGroup(stack, securityGroup.id, securityGroup.props)
                group.addIngressRule(securityGroup.ingressProps.peer, securityGroup.ingressProps.port)
                return group
            }) ?? []

        const albSecurityGroupHTTPS = new ec2.SecurityGroup(stack, 'SecurityGroup-ALB-443', {
            allowAllOutbound: true,
            vpc: template.vpc,
        })
        albSecurityGroupHTTPS.addIngressRule(ec2.Peer.anyIpv4(), ec2.Port.tcp(443))
        albSecurityGroupHTTPS.addIngressRule(ec2.Peer.anyIpv4(), ec2.Port.tcp(80))
        albSecurityGroupHTTPS.addIngressRule(ec2.Peer.anyIpv4(), ec2.Port.tcp(8080))
        albSecurityGroupHTTPS.addIngressRule(ec2.Peer.anyIpv4(), ec2.Port.tcp(8443))

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
            secrets: secrets,
            environment: template.configureDatadog ? Object.assign({}, template.environment, defaultDatadogServiceEnv(template.name)) : template.environment,
            logging: template.configureDatadog ? datadogLogging(template.name) : defaultLogging(),
            dockerLabels: template.configureDatadog ? datadogDockerLabels(template.name) : undefined,
        })
        if (template.configureDatadog) {
            addDataDogContainer(taskDefinition)
        }

        addEventStoreContainer(taskDefinition) //temp solution because cloud version isn't so cheap for testing purposes

        const service = new ecs.FargateService(stack, headerCase(`${template.name}-service`), {
            cluster: template.cluster,
            taskDefinition: taskDefinition,
            desiredCount: 1,
            securityGroups: securityGroups,
            serviceName: template.name,
            assignPublicIp: true
        })

        const alb = new ApplicationLoadBalancer(stack, "psinder-load-balancer", {
            vpc: template.vpc,
            internetFacing: true,
            loadBalancerName: 'psinder-load-balancer',
            http2Enabled: true,
            securityGroup: albSecurityGroupHTTPS,
            ipAddressType: IpAddressType.IPV4,
        })

        const httpsListener = alb.addListener('psinder-load-balancer-https-listener', {
            port: 443,
            open: true,
            protocol: ApplicationProtocol.HTTPS,
            certificates: [certificate]
        })

        httpsListener.addTargets("psinder-load-balancer-https-target", {
            port: 8443,
            protocol: ApplicationProtocol.HTTPS,
            healthCheck: {
                path: '/v1/application-status/health',
                protocol: Protocol.HTTPS,
                port: '8443',
                interval: Duration.seconds(60),
                unhealthyThresholdCount: 4
            },
            targets: [service],
            protocolVersion: ApplicationProtocolVersion.HTTP1,
        })

        new route53.ARecord(stack, 'psinder-api-dns-record', {
            recordName: deployEnv(),
            zone: hostedZone,
            target: route53.RecordTarget.fromAlias(new route53_targets.LoadBalancerTarget(alb)),
            ttl: Duration.minutes(1)
        });

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