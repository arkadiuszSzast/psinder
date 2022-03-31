package com.psinder.plugins

import com.szastarek.ktor.globalrequestdata.GlobalRequestData
import io.ktor.application.Application
import io.ktor.application.install

internal fun Application.configureGlobalRequestData() {
    install(GlobalRequestData)
}
