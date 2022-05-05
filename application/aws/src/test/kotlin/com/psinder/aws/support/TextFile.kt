package com.psinder.aws.support

import com.psinder.file.storage.FileBasePath
import com.psinder.file.storage.FileCandidate
import com.psinder.file.storage.FileExtension
import com.psinder.file.storage.FileKey
import com.psinder.file.storage.StoredFile
import io.ktor.http.Url
import kotlinx.datetime.Instant
import org.litote.kmongo.newId

data class TextFile(
    override val key: FileKey,
    override val basePath: FileBasePath,
    override val fileExtension: FileExtension,
    override val savedAt: Instant
) : StoredFile {

    companion object {
        const val basePath = "text-files"

        fun getCandidate(sourceUrl: Url): FileCandidate {
            return FileCandidate(sourceUrl, FileBasePath(basePath), FileKey(newId<TextFile>()))
        }
    }
}
