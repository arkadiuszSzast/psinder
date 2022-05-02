package com.psinder.aws.s3

import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import com.psinder.shared.file.FileCandidate
import com.psinder.shared.file.FileStorage
import com.psinder.shared.file.StoredFile
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.datetime.Clock
import org.apache.tika.config.TikaConfig
import org.apache.tika.metadata.Metadata

class AwsFileStorage(private val httpClient: HttpClient) : FileStorage {
    override suspend fun upload(fileCandidate: FileCandidate): StoredFile {
        val contentAsByteArray = httpClient.get<ByteArray>(fileCandidate.sourceUrl)
        val tikaConfig = TikaConfig.getDefaultConfig()
        val mediaType = tikaConfig.mimeRepository.detect(contentAsByteArray.inputStream(), Metadata())
        val extension = tikaConfig.mimeRepository.forName(mediaType.toString()).extension
        val savedAt = Clock.System.now()

        return s3Client().use { s3 ->
            s3.putObject {
                PutObjectRequest {
                    bucket = fileCandidate.basePath
                    key = fileCandidate.key
                    body = ByteStream.fromBytes(contentAsByteArray)
                    metadata = s3ObjectMetadata {
                        this.mediaType = mediaType
                        this.extension = extension
                        this.savedAt = savedAt
                    }
                }
            }.let { _ -> S3StoredFile(fileCandidate.key, fileCandidate.basePath, extension, savedAt) }
        }
    }
}
