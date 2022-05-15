package com.psinder.file.storage

import io.ktor.http.Url
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class InMemoryFileStorage(private val urlToExtensionMapper: (Url) -> FileExtension = ::defaultUrlToExtensionMapper) :
    FileStorage {

    private var storage = mutableMapOf<String, StoredFile>()

    override suspend fun uploadPublic(fileCandidate: FileCandidate): StoredFile {
        val extension = urlToExtensionMapper(fileCandidate.sourceUrl)

        val file = object : StoredFile {
            override val key: FileKey = fileCandidate.key
            override val basePath: FileBasePath = fileCandidate.basePath
            override val fileExtension: FileExtension = extension
            override val savedAt: Instant = Clock.System.now()
        }

        storage[getFullPath(file.basePath, file.key)] = file
        return file
    }

    fun fileExists(file: StoredFile): Boolean {
        return fileExists(file.basePath, file.key)
    }

    fun fileExists(basePath: FileBasePath, key: FileKey): Boolean {
        return storage.containsKey(getFullPath(basePath, key))
    }

    private fun getFullPath(basePath: FileBasePath, fileKey: FileKey): String {
        return "${basePath.value}/${fileKey.asString()}"
    }

    fun clear() {
        storage = mutableMapOf()
    }
}

private fun defaultUrlToExtensionMapper(url: Url): FileExtension {
    return FileExtension.PNG
}
