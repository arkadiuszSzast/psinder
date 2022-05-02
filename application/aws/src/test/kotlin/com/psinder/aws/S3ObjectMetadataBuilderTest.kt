package com.psinder.aws

import com.psinder.aws.s3.S3ObjectMetadata
import com.psinder.aws.s3.s3ObjectMetadata
import io.kotest.core.spec.style.DescribeSpec
import org.apache.tika.mime.MediaType
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

class S3ObjectMetadataBuilderTest : DescribeSpec() {

    init {
        describe("S3ObjectMetadataBuilder") {

            it("should build metadata") {
                //arrange && act
                val result = s3ObjectMetadata {
                    mediaType = MediaType("application", "json")
                    extension = "json"
                }

                //assert
                expectThat(result) {
                    get { this[S3ObjectMetadata.Keys.mediaType] }.isEqualTo("application/json")
                    get { this[S3ObjectMetadata.Keys.extension] }.isEqualTo("json")
                    get { this[S3ObjectMetadata.Keys.savedAt] }.isNull()
                }
            }
        }
    }
}
