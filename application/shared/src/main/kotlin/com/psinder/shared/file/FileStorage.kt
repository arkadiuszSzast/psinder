package com.psinder.shared.file

interface FileStorage {
    suspend fun upload(fileCandidate: FileCandidate): StoredFile
}
