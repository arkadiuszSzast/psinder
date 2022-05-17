package com.psinder.dog.commands

import arrow.core.None
import arrow.core.Some
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.psinder.auth.account.AccountId
import com.psinder.auth.authority.impersonateDogFeature
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.config.JwtAuthConfig
import com.psinder.dog.DogAggregate
import com.psinder.dog.DogProfileDto
import com.psinder.dog.ImpersonatingError
import com.psinder.dog.events.DogImpersonatedEvent
import com.psinder.dog.events.DogImpersonatedSuccessfullyEvent
import com.psinder.dog.events.DogImpersonatingFailedEvent
import com.psinder.dog.impersonate
import com.psinder.dog.queries.FindDogProfileByIdQuery
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.shared.jwt.JwtToken
import com.trendyol.kediatr.AsyncCommandWithResultHandler
import com.trendyol.kediatr.CommandBus
import io.traxter.eventstoredb.EventStoreDB
import mu.KotlinLogging
import org.litote.kmongo.Id
import pl.brightinventions.codified.enums.codifiedEnum
import java.util.Date

internal class ImpersonateDogCommandHandler(
    private val jwtConfig: JwtAuthConfig,
    private val commandBus: CommandBus,
    private val eventStore: EventStoreDB,
    private val acl: AuthorizedAccountAbilityProvider,
) : AsyncCommandWithResultHandler<ImpersonateDogCommand, ImpersonateDogCommandResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(command: ImpersonateDogCommand): ImpersonateDogCommandResult {
        acl.ensure().hasAccessTo(impersonateDogFeature)
        val (dogId, accountContext, metadata) = command

        val dog = commandBus.executeQueryAsync(FindDogProfileByIdQuery(dogId)).dog

        return when (dog) {
            is None -> {
                logger.warn { "Dog not found: $dogId. Impersonating failed." }
                ImpersonateDogCommandFailureResult(ImpersonatingError.NotFound.codifiedEnum())
            }
            is Some -> {
                val event = DogAggregate.impersonate(accountContext, dog.value)
                eventStore.appendToStream(
                    event.streamName,
                    event.toEventData(metadata),
                )
                event.toResult()
            }
        }
    }

    private fun DogImpersonatedEvent.toResult(): ImpersonateDogCommandResult {
        return when (this) {
            is DogImpersonatedSuccessfullyEvent -> ImpersonateDogCommandSuccessResult(
                getToken(
                    this.dogId,
                    this.impersonatorId
                )
            )
            is DogImpersonatingFailedEvent -> ImpersonateDogCommandFailureResult(this.error)
        }
    }

    private fun getToken(dogId: Id<DogProfileDto>, accountId: AccountId) =
        JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim("accountId", accountId.value)
            .withClaim("dogId", dogId.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + jwtConfig.expirationInMillis))
            .sign(Algorithm.HMAC256(jwtConfig.secret))
            .let { JwtToken.createOrThrow(it) }
}
