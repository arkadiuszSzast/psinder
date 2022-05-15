package com.psinder.file.storage

interface FileStorage {
    suspend fun uploadPublic(fileCandidate: FileCandidate): StoredFile
}
