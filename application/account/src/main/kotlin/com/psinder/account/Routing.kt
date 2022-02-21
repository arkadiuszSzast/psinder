package com.psinder.account

import com.psinder.account.commands.CreateAccountCommand
import com.psinder.account.commands.LoginAccountCommand
import com.psinder.account.requests.CreateAccountRequest
import com.psinder.account.requests.LoginAccountRequest
import com.psinder.account.responses.LoginAccountResponse
import com.psinder.shared.validation.validateEagerly
import com.trendyol.kediatr.CommandBus
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureAccountRouting() {

    val commandBus: CommandBus by inject()

    routing {
        post("/account") {
            val request = call.receive<CreateAccountRequest>().validateEagerly()
            call.respond(commandBus.executeCommandAsync(CreateAccountCommand(request)))
        }

        post("/login") {
            val request = call.receive<LoginAccountRequest>().validateEagerly()
            val loginCommandResult = commandBus.executeCommandAsync(LoginAccountCommand(request))
            call.respond(LoginAccountResponse(loginCommandResult.token))
        }

        authenticate {
            get("/secret") {
                call.respondText { "Hello from secure" }
            }
        }
    }
}
