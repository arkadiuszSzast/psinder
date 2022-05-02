package com.psinder.account

import com.psinder.account.activation.ActivateAccountFailedResponse
import com.psinder.account.activation.ActivateAccountSuccessfullyResponse
import com.psinder.account.activation.commands.ActivateAccountCommand
import com.psinder.account.activation.commands.ActivateAccountCommandFailure
import com.psinder.account.activation.commands.ActivateAccountCommandSucceed
import com.psinder.account.activation.requests.ActivateAccountRequest
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
import io.ktor.http.HttpStatusCode
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
        post(AccountApi.v1) {
            val request = call.receive<CreateAccountRequest>().validateEagerly()
            call.respond(commandBus.executeCommandAsync(CreateAccountCommand(request)))
        }

        post("${AccountApi.v1}/activate") {
            val request = call.receive<ActivateAccountRequest>().validateEagerly()

            when (val result = commandBus.executeCommandAsync(ActivateAccountCommand(request.token))) {
                is ActivateAccountCommandSucceed -> ActivateAccountSuccessfullyResponse(result.accountId)
                    .let { call.respond(HttpStatusCode.OK, it) }
                is ActivateAccountCommandFailure -> ActivateAccountFailedResponse(result.accountId, result.errorCode)
                    .let { call.respond(HttpStatusCode.BadRequest, it) }
            }
        }

        post("/login") {
            val request = call.receive<LoginAccountRequest>().validateEagerly()
            val loginCommandResult = commandBus.executeCommandAsync(LoginAccountCommand(request))
            call.respond(LoginAccountResponse(loginCommandResult.token))
        }

        get("/test") {
            call.respond("")
        }

        authenticate {
            get("/secret") {
                call.respondText { "Hello from secure" }
            }
        }
    }
}
