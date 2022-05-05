package com.psinder.file.storage.commands

import arrow.core.Either
import com.psinder.file.storage.FileCandidate
import com.psinder.file.storage.StoredFile
import com.trendyol.kediatr.CommandMetadata
import com.trendyol.kediatr.CommandWithResult

data class UploadFileCommand(val fileCandidate: FileCandidate, override val metadata: CommandMetadata? = null) :
    CommandWithResult<UploadFileResult>

data class UploadFileResult(val result: Either<Throwable, StoredFile>)
