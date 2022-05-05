package com.psinder.file.storage

interface FileStorage {
    suspend fun upload(fileCandidate: FileCandidate): StoredFile
}
