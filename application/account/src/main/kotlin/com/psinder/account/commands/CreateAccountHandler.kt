package com.psinder.account.commands

import arrow.core.nel
import com.psinder.account.AccountAggregate
import com.psinder.account.AccountProjection
import com.psinder.account.create
import com.psinder.account.queries.FindAccountByEmailQuery
import com.psinder.auth.authority.authorities
import com.psinder.auth.authority.createAccountFeature
import com.psinder.auth.authority.withInjectedAuthoritiesReturning
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.role.Role
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.shared.validation.ValidationError
import com.psinder.shared.validation.ValidationException
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import com.trendyol.kediatr.CommandBus
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging
import pl.brightinventions.codified.enums.codifiedEnum

internal class CreateAccountHandler(
    private val commandBus: CommandBus,
    private val eventStore: EventStoreDB,
    private val acl: AuthorizedAccountAbilityProvider,
) : AsyncCommandWithResultHandler<CreateAccountCommand, CreateAccountCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: CreateAccountCommand): CreateAccountCommandResult {
        acl.ensure().hasAccessTo(createAccountFeature)
        val (createAccountRequest, metadata) = command
        val (personalData, email, rawPassword, timeZoneId) = createAccountRequest

        logger.debug { "Starting creating account" }

        val alreadyRegisteredAccount = withInjectedAuthoritiesReturning(
            authorities {
                entityAccess(AccountProjection::class) {
                    viewScope { account, _ -> account.email == email }
                }
            }
        ) {
            commandBus.executeQueryAsync(FindAccountByEmailQuery(email)).account
        }

        alreadyRegisteredAccount.tap {
            logger.error { "Cannot create account. Email [${it.email}] is already taken" }
            throw ValidationException(
                ValidationError(
                    ".email",
                    "validation.email_already_taken"
                ).nel()
            )
        }
        val accountAggregateCreatedEvent =
            AccountAggregate.Events.create(
                email,
                personalData,
                Role.User.codifiedEnum(),
                rawPassword.hashpw(),
                timeZoneId
            )

        logger.debug { "Account created. Sending event: $accountAggregateCreatedEvent" }
        eventStore.appendToStream(
            accountAggregateCreatedEvent.streamName,
            accountAggregateCreatedEvent.toEventData(metadata),
        )

        return CreateAccountCommandResult(accountAggregateCreatedEvent.accountId.cast())
    }
}
