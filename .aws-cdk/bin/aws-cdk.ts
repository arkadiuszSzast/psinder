#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from '@aws-cdk/core';

import {psinderTemplate} from "../lib/service-template";
import {NetworkStack} from "../lib/network-stack";
import {ClusterStack} from "../lib/cluster-stack";
import {Service} from "../lib/service-factory";
import {S3BucketsStack} from "../lib/s3-bucket-stack";
import {CloudFrontStack} from "../lib/cloud-front-stack";
import {CertificatesStack} from "../lib/certificates-stack";

const cdkEnv: cdk.Environment = {
    account: process.env.AWS_ACCOUNT_ID,
    region: process.env.AWS_DEFAULT_REGION,
}

const app = new cdk.App();

const networkStack = new NetworkStack(app, {
    env: cdkEnv
})

const certificatesStack = new CertificatesStack(app, {
    env: cdkEnv
})

const clusterStack = new ClusterStack(app, networkStack.vpc, {
    env: cdkEnv
})

const s3BucketsStack = new S3BucketsStack(app, {
    env: cdkEnv
})

const cloudFrontImagesStack = new CloudFrontStack(app, certificatesStack.psinderCertificate, {
    env: cdkEnv
})

const psinderStack = Service.from(app, psinderTemplate(networkStack.vpc, clusterStack.cluster, {
    env: cdkEnv
}), certificatesStack.psinderCertificate)
