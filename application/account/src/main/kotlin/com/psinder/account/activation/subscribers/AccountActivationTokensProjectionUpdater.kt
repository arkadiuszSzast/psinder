package com.psinder.account.activation.subscribers

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import arrow.core.getOrElse
import arrow.core.nel
import com.eventstore.dbclient.RecordedEvent
import com.psinder.account.activation.AccountActivationTokens
import com.psinder.account.activation.AccountActivationTokensRepository
import com.psinder.account.activation.ActivationToken
import com.psinder.account.activation.events.AccountActivationTokenGeneratedEvent
import com.psinder.auth.account.AccountId
import com.psinder.events.getAs
import mu.KotlinLogging

internal class AccountActivationTokensProjectionUpdater(private val accountActivationTokensRepository: AccountActivationTokensRepository) {
    private val log = KotlinLogging.logger {}

    suspend fun update(event: RecordedEvent) {
        when (event.eventType) {
            AccountActivationTokenGeneratedEvent.fullEventType.get() -> applyAccountActivateTokenGeneratedEvent(event.getAs())
        }
    }

    private suspend fun applyAccountActivateTokenGeneratedEvent(event: Either<Throwable, AccountActivationTokenGeneratedEvent>) {
        event.map {
            val accountId = AccountId(it.accountId.toString())
            val accountActivationTokens = accountActivationTokensRepository.findOneByAccountId(accountId)
                .map { foundTokens -> AccountActivationTokens.appendToken(foundTokens, ActivationToken(it.token)) }
                .getOrElse { AccountActivationTokens.create(accountId, ActivationToken(it.token).nel()) }

            log.info("Stream group: account-activation-tokens-projection-updater appended new token for account: ${it.accountId}")
            accountActivationTokensRepository.save(accountActivationTokens)
        }.bind()
    }
}
