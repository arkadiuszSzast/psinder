package com.psinder.aws.s3

import aws.sdk.kotlin.services.s3.S3Client

suspend fun s3Client(): S3Client = S3Client.fromEnvironment()
