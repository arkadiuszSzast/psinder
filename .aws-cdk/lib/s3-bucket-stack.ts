import * as cdk from '@aws-cdk/core';
import {applicationName, deployEnv, envSpecificName, zoneName} from "./utils";
import * as s3 from "@aws-cdk/aws-s3";

export class S3BucketsStack extends cdk.Stack {

    constructor(scope: cdk.Construct, props?: cdk.StackProps) {
        super(scope, envSpecificName(applicationName() + '-s3'), props)

        const bucketsNames: string[] = ['dog-profile-images']

        bucketsNames.map((bucketName) => {
            const bucketEnvSpecificName = envSpecificName(bucketName)

            const bucket = new s3.Bucket(this, `${bucketEnvSpecificName}-bucket`, {
                bucketName: bucketEnvSpecificName,
                removalPolicy: cdk.RemovalPolicy.DESTROY,
            })

            const tags = cdk.Tags.of(bucket)
            tags.add('application', applicationName())
            tags.add('env', deployEnv())
        })
    }

}