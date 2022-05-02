package com.psinder.aws.s3

import com.psinder.shared.file.StoredFile
import kotlinx.datetime.Instant

data class S3StoredFile(
    override val key: String,
    override val basePath: String,
    override val fileExtension: String,
    override val savedAt: Instant
) : StoredFile
