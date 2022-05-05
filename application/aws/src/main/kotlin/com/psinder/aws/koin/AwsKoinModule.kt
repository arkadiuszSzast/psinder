package com.psinder.aws.koin

import aws.sdk.kotlin.runtime.endpoint.AwsEndpoint
import aws.sdk.kotlin.runtime.endpoint.StaticEndpointResolver
import aws.sdk.kotlin.services.s3.S3Client
import com.psinder.aws.config.AWSConfig
import com.psinder.aws.s3.AwsFileStorage
import com.psinder.aws.s3.BucketNameResolver
import com.psinder.aws.s3.EnvBucketNameResolver
import com.psinder.file.storage.FileStorage
import com.psinder.shared.config.ApplicationConfig
import com.psinder.shared.provider.Provider
import io.ktor.client.HttpClient
import org.koin.dsl.bind
import org.koin.dsl.module

val awsKoinModule = module {
    single {
        when (AWSConfig.useLocalstack) {
            true -> {
                Provider {
                    S3Client.fromEnvironment {
                        endpointResolver = StaticEndpointResolver(AwsEndpoint("http://localhost:4566"))
                    }
                }
            }
            else -> Provider { S3Client.fromEnvironment() }
        }
    }
    single { EnvBucketNameResolver(ApplicationConfig) } bind BucketNameResolver::class
    single { AwsFileStorage(HttpClient(), get(), get()) } bind FileStorage::class
}
