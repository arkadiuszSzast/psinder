package com.psinder.aws.s3

fun interface BucketNameResolver {
    fun resolve(bucketName: BucketName): ResolvedBucketName
}
