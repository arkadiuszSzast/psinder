package com.psinder.aws.s3

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import com.psinder.file.storage.FileCandidate
import com.psinder.file.storage.FileExtension
import com.psinder.file.storage.FileStorage
import com.psinder.file.storage.StoredFile
import com.psinder.shared.provider.Provider
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.datetime.Clock
import org.apache.tika.config.TikaConfig
import org.apache.tika.metadata.Metadata

class AwsFileStorage(
    private val httpClient: HttpClient,
    private val s3ClientProvider: Provider<S3Client>,
    private val bucketNameResolver: BucketNameResolver
) : FileStorage {
    override suspend fun upload(fileCandidate: FileCandidate): StoredFile {
        val bucketName = bucketNameResolver.resolve(BucketName(fileCandidate.basePath.value))
        val contentAsByteArray = httpClient.get<ByteArray>(fileCandidate.sourceUrl)
        val tikaConfig = TikaConfig.getDefaultConfig()
        val mediaType = tikaConfig.mimeRepository.detect(contentAsByteArray.inputStream(), Metadata())
        val extension = tikaConfig.mimeRepository.forName(mediaType.toString()).extension
        val savedAt = Clock.System.now()

        return s3ClientProvider.get().use { s3 ->
            s3.putObject(
                PutObjectRequest {
                    bucket = bucketName.resolvedValue
                    key = fileCandidate.key.asString()
                    body = ByteStream.fromBytes(contentAsByteArray)
                    metadata = s3ObjectMetadata {
                        this.mediaType = mediaType
                        this.extension = extension
                        this.savedAt = savedAt
                    }
                }
            ).let { _ -> S3StoredFile(fileCandidate.key, fileCandidate.basePath, FileExtension(extension), savedAt) }
        }
    }
}
