package com.psinder.file.storage

import io.ktor.http.Url

fun interface PublicFileUrlResolver {
    fun resolve(file: StoredFile): Url
}
