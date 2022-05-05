package com.psinder.aws.s3

import com.psinder.file.storage.FileBasePath
import com.psinder.file.storage.FileExtension
import com.psinder.file.storage.FileKey
import com.psinder.file.storage.StoredFile
import kotlinx.datetime.Instant

data class S3StoredFile(
    override val key: FileKey,
    override val basePath: FileBasePath,
    override val fileExtension: FileExtension,
    override val savedAt: Instant
) : StoredFile
