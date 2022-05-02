package com.psinder.aws.koin

import com.psinder.aws.s3.AwsFileStorage
import com.psinder.shared.file.FileStorage
import io.ktor.client.HttpClient
import org.koin.dsl.bind
import org.koin.dsl.module

val awsKoinModule = module {
    single { AwsFileStorage(HttpClient()) } bind FileStorage::class
}
