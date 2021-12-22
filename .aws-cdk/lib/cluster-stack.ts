import * as cdk from '@aws-cdk/core';
import * as ecs from '@aws-cdk/aws-ecs';
import * as ec2 from "@aws-cdk/aws-ec2";
import {applicationName, deployEnv, envSpecificName, zoneName} from "./utils";

export class ClusterStack extends cdk.Stack {
    readonly cluster: ecs.ICluster

    constructor(scope: cdk.Construct, vpc: ec2.Vpc, props?: cdk.StackProps) {
        super(scope, envSpecificName(applicationName() + '-cluster'), props)

        this.cluster = new ecs.Cluster(this, "Cluster", {
            clusterName: envSpecificName(applicationName()),
            containerInsights: true,
            vpc: vpc,
            defaultCloudMapNamespace: {
                name: zoneName()
            }
        })

        const clusterTags = cdk.Tags.of(this.cluster)
        clusterTags.add('application', applicationName())
        clusterTags.add('env', deployEnv())
    }

}