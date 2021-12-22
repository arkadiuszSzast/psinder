package com.psinder.account

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.config.JwtConfig
import com.psinder.database.database
import com.psinder.shared.EmailAddress
import com.psinder.shared.password.Password
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import mu.KotlinLogging
import org.litote.kmongo.newId
import java.util.*

fun Application.configureAccountRouting() {

    val logger = KotlinLogging.logger {}

    routing {
        get("/me") {
            logger.info { "Entered me method" }
            call.respondText("Hello from me")
        }
        get("/oups") {
            logger.info { "Entered oups method" }
            throw Error("oups")
            call.respondText("Hello from me")
        }
        post( "/register" ) {
            val request = call.receive<RegisterRequest>()
            database.getCollection<Account>().insertOne(
                Account(
                    newId(),
                    EmailAddress.create("test@test.com").toOption().orNull()!!,
                    "zxc",
                    Password.create("qwe123#@!123").toOption().orNull()!!
                )
            )
            call.respondText { "OK" }
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