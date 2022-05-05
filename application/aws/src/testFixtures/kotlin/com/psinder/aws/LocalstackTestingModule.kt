package com.psinder.aws

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.runtime.endpoint.AwsEndpoint
import aws.sdk.kotlin.runtime.endpoint.StaticEndpointResolver
import aws.sdk.kotlin.services.s3.S3Client
import aws.smithy.kotlin.runtime.auth.awscredentials.Credentials
import aws.smithy.kotlin.runtime.http.endpoints.Endpoint
import com.psinder.shared.provider.Provider
import org.koin.dsl.module

val localstackTestingModule = module {
    single {
        Provider {
            S3Client.invoke {
                endpointResolver = StaticEndpointResolver(AwsEndpoint(Endpoint(LocalstackContainer.s3Endpoint)))
                credentialsProvider =
                    StaticCredentialsProvider(Credentials(LocalstackContainer.accessKey, LocalstackContainer.secretKey))
                region = LocalstackContainer.region
            }
        }
    }
}
