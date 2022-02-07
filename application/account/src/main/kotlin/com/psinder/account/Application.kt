package com.psinder.account

import io.ktor.application.Application

@Suppress("unused")
fun Application.accountModule() {
    configureAccountRouting()
    configureEventStoreSubscribers()
}