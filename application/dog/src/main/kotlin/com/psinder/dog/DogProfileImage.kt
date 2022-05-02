package com.psinder.dog

import com.psinder.shared.file.FileCandidate
import com.psinder.shared.file.StoredFile
import io.ktor.http.Url
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.litote.kmongo.newId

@Serializable
data class DogProfileImage(
    override val fileExtension: String,
    override val savedAt: Instant,
    override val key: String,
    override val basePath: String
) : StoredFile {

    companion object {
        const val basePath = "dog-profile-images"

        fun getCandidate(sourceUrl: Url): FileCandidate {
            return FileCandidate(sourceUrl, basePath, newId<DogProfileImage>().toString())
        }
    }
}
