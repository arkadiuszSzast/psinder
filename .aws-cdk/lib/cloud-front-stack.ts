import * as cdk from '@aws-cdk/core';
import {applicationName, deployEnv, envSpecificName, zoneName} from "./utils";
import * as cloudfront from "@aws-cdk/aws-cloudfront";
import * as origins from "@aws-cdk/aws-cloudfront-origins";
import * as targets from "@aws-cdk/aws-route53-targets";
import * as route53 from "@aws-cdk/aws-route53";
import * as acm from "@aws-cdk/aws-certificatemanager";
import * as s3 from "@aws-cdk/aws-s3";
import {Duration} from "@aws-cdk/core";

export class CloudFrontStack extends cdk.Stack {

    constructor(scope: cdk.Construct, props?: cdk.StackProps) {
        super(scope, envSpecificName(applicationName() + '-cloud-front'), props)

        const hostedZone = route53.HostedZone.fromLookup(this, "psinder-hosted-zone", {
            domainName: 'psinder.link'
        })
        const bucketEnvSpecificName = envSpecificName('dog-profile-images')
        const certificate = acm.Certificate.fromCertificateArn(this, 'psinder-certificate', 'arn:aws:acm:us-east-1:993160204208:certificate/47443f83-e7db-4f4e-8e45-7b5751967df8')

        const bucket = s3.Bucket.fromBucketName(this, `${bucketEnvSpecificName}-bucket`, bucketEnvSpecificName)

        const cf = new cloudfront.Distribution(this, "cdnDistribution", {
            defaultBehavior: {origin: new origins.S3Origin(bucket)},
            domainNames: [`${deployEnv()}-images.psinder.link`],
            certificate: certificate,
        });
        cf.addBehavior(`/dog-profile-images`, new origins.S3Origin(bucket))

        new route53.ARecord(this, "CDNARecord", {
            recordName: `${deployEnv()}-images`,
            zone: hostedZone,
            target: route53.RecordTarget.fromAlias(new targets.CloudFrontTarget(cf)),
            ttl: Duration.minutes(1)
        });

        new route53.AaaaRecord(this, "AliasRecord", {
            recordName: `${deployEnv()}-images`,
            zone: hostedZone,
            target: route53.RecordTarget.fromAlias(new targets.CloudFrontTarget(cf)),
            ttl: Duration.minutes(1)
        });

        const tags = cdk.Tags.of(cf)
        tags.add('application', applicationName())
        tags.add('env', deployEnv())
    }
}