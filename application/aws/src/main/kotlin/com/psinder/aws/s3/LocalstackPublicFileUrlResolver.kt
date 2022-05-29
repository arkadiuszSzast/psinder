package com.psinder.aws.s3

import com.psinder.file.storage.PublicFileUrlResolver
import com.psinder.file.storage.StoredFile
import io.ktor.http.Url

class LocalstackPublicFileUrlResolver(private val bucketNameResolver: BucketNameResolver) : PublicFileUrlResolver {
    override fun resolve(file: StoredFile): Url {
        val resolvedBucketName = bucketNameResolver.resolve(BucketName(file.basePath.value))
        return Url("http://localhost:4566/${resolvedBucketName.resolvedValue}/${file.key.value}")
    }
}
