import * as cdk from '@aws-cdk/core';
import {applicationName, deployEnv, envSpecificName, zoneName} from "./utils";
import * as acm from "@aws-cdk/aws-certificatemanager";
import * as route53 from "@aws-cdk/aws-route53";

export class CertificatesStack extends cdk.Stack {
    readonly psinderCertificate: acm.Certificate

    constructor(scope: cdk.Construct, props?: cdk.StackProps) {
        super(scope, envSpecificName(applicationName() + '-certificates'), props)

        const hostedZone = route53.HostedZone.fromLookup(this, "psinder-hosted-zone", {
            domainName: 'psinder.link'
        })
        this.psinderCertificate = new acm.Certificate(this, 'Certificate', {
            domainName: '*.psinder.link',
            validation: acm.CertificateValidation.fromDns(hostedZone)
        })

        const tags = cdk.Tags.of(this.psinderCertificate)
        tags.add('application', applicationName())
        tags.add('env', deployEnv())
    }

}