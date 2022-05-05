package com.psinder.file.storage.koin

import com.psinder.file.storage.commands.UploadFileCommandHandler
import org.koin.dsl.module

val fileStorageCommandHandlersModule = module {
    single { UploadFileCommandHandler(get()) }
}
