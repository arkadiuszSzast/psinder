package com.psinder.account

import an.awesome.pipelinr.Pipeline
import com.psinder.account.commands.CreateAccountCommand
import com.psinder.account.commands.LoginAccountCommand
import com.psinder.account.requests.CreateAccountRequest
import com.psinder.account.requests.LoginAccountRequest
import com.psinder.account.responses.LoginAccountResponse
import com.psinder.shared.validation.validateEagerly
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureAccountRouting() {

    val pipeline: Pipeline by inject()

    routing {
        post("/account") {
            val request = call.receive<CreateAccountRequest>().validateEagerly()
            call.respond(pipeline.send(CreateAccountCommand(request)))
        }
        post("/login") {
            val request = call.receive<LoginAccountRequest>()
            val loginCommandResult = pipeline.send(LoginAccountCommand(request))
            call.respond(LoginAccountResponse(loginCommandResult.token))
        }
    }
}
