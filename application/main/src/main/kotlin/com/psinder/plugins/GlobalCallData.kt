package com.psinder.plugins

import com.github.maaxgr.ktor.globalcalldata.GlobalCallData
import io.ktor.application.Application
import io.ktor.application.install

internal fun Application.configureGlobalCallData() {
    install(GlobalCallData)
}
