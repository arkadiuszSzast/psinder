package com.psinder.file.storage

import io.ktor.http.Url

data class FileCandidate(
    val sourceUrl: Url,
    override val basePath: FileBasePath,
    override val key: FileKey
) : HasFilePath
