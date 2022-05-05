package com.psinder.aws.s3

import aws.sdk.kotlin.services.s3.S3Client
import com.psinder.aws.config.AWSConfig
import com.psinder.shared.koin.getKoinInstance
import org.koin.core.qualifier.named

fun s3Client(): S3Client {
    return when (AWSConfig.useLocalstack) {
        // true -> S3Client.fromEnvironment { endpointResolver = StaticEndpointResolver(AwsEndpoint("http://localstack:4566")) }
        // else -> S3Client.fromEnvironment()
        true -> getKoinInstance(named("localstack"))
        else -> getKoinInstance()
    }
}
