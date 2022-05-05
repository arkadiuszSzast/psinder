package com.psinder.aws.s3

import com.psinder.shared.config.ApplicationConfig
import mu.KotlinLogging

internal class EnvBucketNameResolver(private val applicationConfig: ApplicationConfig) : BucketNameResolver {
    private val logger = KotlinLogging.logger {}

    override fun resolve(bucketName: BucketName): ResolvedBucketName {
        logger.debug { "Resolving bucket name: $bucketName" }
        val resolved = "${applicationConfig.environment}-${bucketName.value}"
        logger.debug { "Resolved bucket name: $resolved" }
        return ResolvedBucketName(resolved)
    }
}
