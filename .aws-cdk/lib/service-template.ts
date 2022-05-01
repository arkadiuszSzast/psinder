import {SecurityGroupIdAndProps, ServiceTemplate} from "./service-factory";
import * as ec2 from "@aws-cdk/aws-ec2";
import * as ecs from "@aws-cdk/aws-ecs";
import {RepositoryName} from "./repository-name";
import * as cdk from "@aws-cdk/core";
import {deployEnv, githubSha} from "./utils";

export function psinderTemplate(vpc: ec2.IVpc, cluster: ecs.ICluster, stackProps: cdk.StackProps): ServiceTemplate {

    const securityGroup: SecurityGroupIdAndProps = {
        id: 'Psinder-Security-Group',
        props: {
            description: 'Allow to access psinder service on port 8080',
            securityGroupName: 'psinder-port-8080',
            vpc: vpc
        },
        ingressProps: {
            peer: ec2.Peer.anyIpv4(),
            port: ec2.Port.tcp(8080)
        }
    }
    const httpsSecurityGroup: SecurityGroupIdAndProps = {
        id: 'Psinder-HTTPS-Security-Group',
        props: {
            description: 'Allow to access psinder service on port 8443',
            securityGroupName: 'psinder-https-port-8443',
            vpc: vpc
        },
        ingressProps: {
            peer: ec2.Peer.anyIpv4(),
            port: ec2.Port.tcp(8443)
        }
    }

    return {
        vpc: vpc,
        cluster: cluster,
        name: 'psinder',
        port: [8443],
        repository: RepositoryName.PSINDER,
        configureDatadog: true,
        stackProps: stackProps,
        assignPublicIp: false,
        securityGroups: [securityGroup, httpsSecurityGroup],
        secrets: {
            DB_CONNECTION_STRING: '/psinder/db-connection-string',
            KEYSTORE_PASSWORD: '/psinder/keystore/password',
            KEYSTORE_KEY_PASSWORD: '/psinder/keystore/key-password',
            SENTRY_DSN: '/psinder/sentry/dsn',
            SENDGRID_API_KEY: '/psinder/sendgrid/apiKey',
            AWS_ACCESS_KEY_ID: '/psinder/aws/accessKey',
            AWS_SECRET_ACCESS_KEY: '/psinder/aws/secretKey',
        },
        environment: {
            'DEPLOY_ENV': deployEnv(),
            'VCS_VERSION': githubSha(),
            'SENTRY_ENABLED': 'true',
            'KEYSTORE_PATH': 'keystore/keystore-aws.jks',
        }
    }
}
