package com.psinder.shared.file

import kotlinx.datetime.Instant

interface StoredFile {
    val key: String
    val basePath: String
    val fileExtension: String
    val savedAt: Instant
}