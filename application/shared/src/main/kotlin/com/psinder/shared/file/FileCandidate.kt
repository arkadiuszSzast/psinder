package com.psinder.shared.file

import io.ktor.http.Url

data class FileCandidate(
    val sourceUrl: Url,
    val basePath: String,
    val key: String
)
