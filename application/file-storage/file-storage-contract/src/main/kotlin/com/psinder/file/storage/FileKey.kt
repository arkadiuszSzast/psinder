package com.psinder.file.storage

import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class FileKey(val value: Id<out HasFilePath>) {
    fun asString(): String = value.toString()
}
