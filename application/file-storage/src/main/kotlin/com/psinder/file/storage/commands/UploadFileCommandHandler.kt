package com.psinder.file.storage.commands

import arrow.core.Either
import com.psinder.file.storage.FileStorage
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import mu.KotlinLogging

internal class UploadFileCommandHandler(private val fileStorage: FileStorage) :
    AsyncCommandWithResultHandler<UploadFileCommand, UploadFileResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: UploadFileCommand): UploadFileResult {
        val (fileCandidate) = command
        logger.debug { "Going to upload file $fileCandidate" }

        return Either.catch { fileStorage.uploadPublic(command.fileCandidate) }
            .tap { logger.debug { "Uploaded file $it" } }
            .tapLeft { logger.error { "Error while uploading file: $fileCandidate. Exception: ${it.stackTraceToString()}" } }
            .let { UploadFileResult(it) }
    }
}
