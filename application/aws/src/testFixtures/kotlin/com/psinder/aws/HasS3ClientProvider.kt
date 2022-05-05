package com.psinder.aws

import aws.sdk.kotlin.services.s3.S3Client
import com.psinder.shared.provider.Provider

interface HasS3ClientProvider {
    val s3ClientProvider: Provider<S3Client>
}
