package com.psinder.aws

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.CreateBucketRequest
import aws.sdk.kotlin.services.s3.model.DeleteBucketRequest
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.ListBucketsRequest
import com.psinder.aws.s3.BucketName
import com.psinder.shared.provider.Provider
import io.kotest.common.runBlocking
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.inject

abstract class LocalStackTest(private val neededModules: List<Module>, private val buckets: List<BucketName>) :
    KoinTest, HasS3ClientProvider, DescribeSpec() {

    override val s3ClientProvider: Provider<S3Client> by inject()

    override fun beforeEach(testCase: TestCase) {
        removeAllBuckets()
        createBuckets()
    }

    private fun removeAllBuckets() {
        runBlocking {
            s3ClientProvider.get().use { s3Client ->
                val buckets = s3Client.listBuckets { ListBucketsRequest {} }.buckets.orEmpty()
                buckets.map { it.name }.forEach { bucketName ->
                    s3Client.listObjects { ListBucketsRequest { bucket = bucketName } }.contents.orEmpty().forEach {
                        s3Client.deleteObject(DeleteObjectRequest { bucket = bucketName; key = it.key })
                    }
                    s3Client.deleteBucket(DeleteBucketRequest { bucket = bucketName })
                }
            }
        }
    }

    private fun createBuckets() {
        runBlocking {
            s3ClientProvider.get().use { s3Client ->
                buckets.forEach { bucketName ->
                    s3Client.createBucket(CreateBucketRequest { bucket = bucketName.value })
                }
            }
        }
    }

    override fun afterSpec(spec: Spec) {
        stopKoin()
    }

    init {
        startKoin { modules(neededModules.toList().plus(localstackTestingModule)) }
    }
}
