package com.psinder.aws.s3

import com.psinder.shared.config.ApplicationConfig
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.system.withEnvironment
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class EnvBucketNameResolverTest : DescribeSpec() {

    init {

        describe("EnvBucketNameResolver") {

            it("should resolve bucket name") {
                withEnvironment("application.env", "dev") {
                    // arrange
                    val resolver = EnvBucketNameResolver(ApplicationConfig)

                    // act
                    val result = resolver.resolve(BucketName("temp-images"))

                    // assert
                    expectThat(result)
                        .get { resolvedValue }.isEqualTo("dev-temp-images")
                }
            }
        }
    }
}
