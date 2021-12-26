package com.psinder.account

import an.awesome.pipelinr.Pipeline
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.account.commands.CreateAccountCommand
import com.psinder.account.requests.CreateAccountRequest
import com.psinder.config.JwtConfig
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import org.koin.ktor.ext.inject
import java.util.Date

fun Application.configureAccountRouting() {

    val pipeline: Pipeline by inject()

    routing {
        post("/account") {
            val request = call.receive<CreateAccountRequest>()
            call.respond(pipeline.send(CreateAccountCommand(request)))
        }
        post("/login") {
            val request = call.receive<LoginRequest>()
            val token = JWT.create()
                .withAudience(JwtConfig.audience)
                .withIssuer(JwtConfig.issuer)
                .withClaim("username", request.login)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(JwtConfig.secret))

            call.respond(LoginResponse(token))
        }
    }
}
