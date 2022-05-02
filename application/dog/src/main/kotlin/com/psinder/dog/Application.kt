package com.psinder.dog

import io.ktor.application.Application

@Suppress("unused")
fun Application.dogModule() {
    configureDogRouting()
}
