package com.psinder.file.storage

import io.ktor.http.Url
import kotlinx.datetime.Instant
import org.litote.kmongo.newId

data class ImageFile(
    override val key: FileKey,
    override val basePath: FileBasePath,
    override val fileExtension: FileExtension,
    override val savedAt: Instant
) : StoredFile {

    companion object {
        const val basePath = "images"

        fun getCandidate(sourceUrl: Url): FileCandidate {
            return FileCandidate(sourceUrl, FileBasePath(basePath), FileKey(newId<ImageFile>()))
        }
    }
}
