package com.psinder.dog

import com.psinder.file.storage.FileBasePath
import com.psinder.file.storage.FileCandidate
import com.psinder.file.storage.FileExtension
import com.psinder.file.storage.FileKey
import com.psinder.file.storage.StoredFile
import io.ktor.http.Url
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.litote.kmongo.newId

@Serializable
data class DogProfileImage(
    override val fileExtension: FileExtension,
    override val savedAt: Instant,
    override val key: FileKey,
    override val basePath: FileBasePath
) : StoredFile {

    companion object {
        const val basePath = "dog-profile-images"

        fun getCandidate(sourceUrl: Url): FileCandidate {
            return FileCandidate(sourceUrl, FileBasePath(basePath), FileKey(newId<DogProfileImage>()))
        }
    }
}
