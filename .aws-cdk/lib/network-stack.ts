import * as cdk from '@aws-cdk/core';
import * as ec2 from '@aws-cdk/aws-ec2';
import {applicationName, deployEnv, envSpecificName, vpcCidr} from "./utils";

export class NetworkStack extends cdk.Stack {
    readonly vpc: ec2.Vpc

    constructor(scope: cdk.Construct, props?: cdk.StackProps) {
        super(scope, envSpecificName(applicationName() + '-network'), props);

        this.vpc = new ec2.Vpc(this, "VPC", {
            cidr: vpcCidr(),
            maxAzs: 2,
            natGateways: 1,
            enableDnsSupport: true,
            enableDnsHostnames: true,
            subnetConfiguration: [
                {
                    cidrMask: 25,
                    name: envSpecificName(applicationName() + '-private-subnet'),
                    subnetType: ec2.SubnetType.PRIVATE,
                },
                {
                    cidrMask: 25,
                    name: envSpecificName(applicationName() + '-public-subnet'),
                    subnetType: ec2.SubnetType.PUBLIC
                }
            ]
        })

        const vpcTags = cdk.Tags.of(this.vpc)
        vpcTags.add('application', applicationName())
        vpcTags.add('env', deployEnv())

    }
}
