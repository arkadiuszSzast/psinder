package com.psinder.file.storage

import kotlinx.datetime.Instant

interface StoredFile : HasFilePath {
    override val key: FileKey
    override val basePath: FileBasePath
    val fileExtension: FileExtension
    val savedAt: Instant
}
