package com.psinder.account.activation.commands

import com.psinder.auth.authority.generateAccountActivationLinkFeature
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.shared.config.ApplicationConfig
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import com.trendyol.kediatr.CommandBus
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import mu.KotlinLogging

internal class GenerateAccountActivationLinkHandler(
    private val appConfig: ApplicationConfig,
    private val commandBus: CommandBus,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncCommandWithResultHandler<GenerateAccountActivationLinkCommand, GenerateAccountActivationLinkCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: GenerateAccountActivationLinkCommand): GenerateAccountActivationLinkCommandResult {
        acl.ensure().hasAccessTo(generateAccountActivationLinkFeature)
        val (accountId, metadata) = command

        logger.debug { "Generating account activation link for accountId: $accountId" }
        val token = commandBus.executeCommandAsync(GenerateAccountActivationTokenCommand(accountId, metadata)).token

        return GenerateAccountActivationLinkCommandResult(buildActivateUrl(token))
    }

    private fun buildActivateUrl(token: JwtToken): Url {
        val appSelfUrl = appConfig.selfUrl
        return URLBuilder(appSelfUrl).path("account", "activate").apply {
            parameters.append("token", token.token)
        }.build()
    }
}
