import * as cdk from '@aws-cdk/core';
import {applicationName, deployEnv, envSpecificName, zoneName} from "./utils";
import * as cloudfront from "@aws-cdk/aws-cloudfront";
import * as origins from "@aws-cdk/aws-cloudfront-origins";
import * as targets from "@aws-cdk/aws-route53-targets";
import * as route53 from "@aws-cdk/aws-route53";
import * as acm from "@aws-cdk/aws-certificatemanager";
import * as s3 from "@aws-cdk/aws-s3";

export class CloudFrontCertificateStack extends cdk.Stack {
    readonly cloudFrontCertificate: acm.Certificate

    constructor(scope: cdk.Construct, props?: cdk.StackProps) {
        super(scope, envSpecificName(applicationName() + '-cloud-front-certificate'), props)

        const hostedZone = route53.HostedZone.fromLookup(this, "psinder-hosted-zone", {
            domainName: 'psinder.link'
        })
        this.cloudFrontCertificate = new acm.Certificate(this, 'Certificate', {
            domainName: '*.psinder.link',
            validation: acm.CertificateValidation.fromDns(hostedZone)
        });

        const tags = cdk.Tags.of(this.cloudFrontCertificate)
        tags.add('application', applicationName())
        tags.add('env', deployEnv())
    }
}