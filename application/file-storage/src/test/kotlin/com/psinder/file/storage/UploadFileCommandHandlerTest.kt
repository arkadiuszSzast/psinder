package com.psinder.file.storage

import com.psinder.file.storage.commands.UploadFileCommand
import com.psinder.file.storage.commands.UploadFileCommandHandler
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.ktor.http.Url
import strikt.api.expectThat
import strikt.arrow.isLeft
import strikt.arrow.isRight
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class UploadFileCommandHandlerTest : DescribeSpec() {

    private val fileStorage = InMemoryFileStorage()
    private val handler = UploadFileCommandHandler(fileStorage)

    override fun beforeEach(testCase: TestCase) {
        fileStorage.clear()
    }

    init {

        describe("UploadFileCommandHandler") {

            it("should upload file") {
                // arrange
                val fileCandidate = ImageFile.getCandidate(Url("http://localhost/image.png"))

                // act
                val result = handler.handleAsync(UploadFileCommand(fileCandidate))

                // act
                expectThat(result)
                    .get { this.result }
                    .isRight()
                    .get { value }
                    .and { get { fileExtension }.isEqualTo(FileExtension.PNG) }
                    .and { get { key }.isEqualTo(fileCandidate.key) }
                    .and { get { basePath }.isEqualTo(fileCandidate.basePath) }

                val fileExistsInStorage = fileStorage.fileExists(result.result.orNull()!!)
                expectThat(fileExistsInStorage).isTrue()
            }

            it("should return exception") {
                // arrange
                val throwingFileStorage = InMemoryFileStorage { throw RuntimeException("Error while storing file") }
                val throwingHandler = UploadFileCommandHandler(throwingFileStorage)
                val fileCandidate = ImageFile.getCandidate(Url("http://localhost/image.png"))

                // act
                val result = throwingHandler.handleAsync(UploadFileCommand(fileCandidate))

                // assert
                expectThat(result)
                    .get { this.result }
                    .isLeft()
                    .get { value }
                    .isA<RuntimeException>()
                    .get { message }.isEqualTo("Error while storing file")

                val fileExistsInStorage = fileStorage.fileExists(fileCandidate.basePath, fileCandidate.key)
                expectThat(fileExistsInStorage).isFalse()
            }
        }
    }
}
