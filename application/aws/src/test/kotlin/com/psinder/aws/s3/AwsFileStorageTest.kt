package com.psinder.aws.s3

import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.ListObjectsRequest
import aws.smithy.kotlin.runtime.content.toByteArray
import com.psinder.aws.LocalStackTest
import com.psinder.aws.support.TextFile
import com.psinder.test.utils.isUpToOneSecondOld
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.ClientRequestException
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import kotlinx.datetime.Instant
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

class AwsFileStorageTest : LocalStackTest(emptyList(), listOf(BucketName(TextFile.basePath))) {

    private val textFileAsByteArray = ClassLoader.getSystemResource("example-text.txt").readBytes()
    private val mockEngine = MockEngine { request ->
        when (request.url.toString()) {
            "https://example-text.test/" -> respond(textFileAsByteArray)
            else -> respondError(HttpStatusCode.NotFound, "Not found")
        }
    }
    private val httpClient = HttpClient(mockEngine)
    private val awsFileStorage =
        AwsFileStorage(httpClient, s3ClientProvider) { bucketName -> ResolvedBucketName(bucketName.value) }

    init {

        describe("AwsFileStorage") {

            it("should upload file") {
                // arrange
                val fileCandidate = TextFile.getCandidate(Url("https://example-text.test"))

                // act
                val result = awsFileStorage.uploadPublic(fileCandidate)

                // assert
                val savedFile = s3ClientProvider.get().use { s3Client ->
                    s3Client.getObject(
                        GetObjectRequest {
                            bucket = result.basePath.value
                            key = result.key.asString()
                        }
                    ) { it }
                }

                val savedFileAsByteArray = savedFile.body?.toByteArray()
                expectThat(savedFile.contentType).isEqualTo("text/plain")
                expectThat(savedFileAsByteArray) { isEqualTo(textFileAsByteArray) }
                expectThat(S3ObjectMetadata.create(savedFile.metadata!!)) {
                    get { this[S3ObjectMetadata.Keys.mediaType] }.isEqualTo("text/plain")
                    get { this[S3ObjectMetadata.Keys.extension] }.isEqualTo(".txt")
                    get { this[S3ObjectMetadata.Keys.extension] }.isEqualTo(".txt")
                    get { Instant.parse(this[S3ObjectMetadata.Keys.savedAt]!!) }.isUpToOneSecondOld()
                }
            }

            it("should throw exception when cannot fetch file from sourceUrl") {
                // arrange
                val fileCandidate = TextFile.getCandidate(Url("https://not-existing.test"))

                // act && assert
                expectThrows<ClientRequestException> {
                    awsFileStorage.uploadPublic(fileCandidate)
                }

                val allObjects = s3ClientProvider.get()
                    .listObjects(ListObjectsRequest { bucket = fileCandidate.basePath.value }).contents.orEmpty()
                expectThat(allObjects) { isEmpty() }
            }
        }
    }
}
