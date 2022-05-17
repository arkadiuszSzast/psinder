package com.psinder.file.storage

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class FileKey(@Contextual val value: Id<out HasFilePath>) {
    fun asString(): String = value.toString()
}
