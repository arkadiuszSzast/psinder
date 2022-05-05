package com.psinder.file.storage

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class FileExtension(val value: String) {
    companion object {
        val PNG = FileExtension("png")
    }
}
