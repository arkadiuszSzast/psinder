package com.psinder.aws

import aws.sdk.kotlin.runtime.auth.credentials.EnvironmentCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client

object S3Client {
    val client by lazy {
        S3Client {
            credentialsProvider = EnvironmentCredentialsProvider()
            region = "eu-north-1"
        }
    }
}