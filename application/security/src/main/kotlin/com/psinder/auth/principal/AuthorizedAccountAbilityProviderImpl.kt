package com.psinder.auth.principal

import arrow.core.getOrElse
import com.psinder.auth.authority.Feature
import com.psinder.shared.kClass
import com.psinder.shared.kClassSimpleName
import mu.KotlinLogging
import kotlin.reflect.KClass

class AuthorizedAccountAbilityProviderImpl(
    private val authenticatedAccountProvider: AuthenticatedAccountProvider
) : AuthorizedAccountAbilityProvider {
    private val logger = KotlinLogging.logger {}

    override suspend fun hasAccessTo(feature: Feature): Boolean {
        return authenticatedAccountProvider.authorities()
            .findFeature(feature)
            .tapNone {
                val accountId = currentPrincipal().accountId
                logger.warn("Account with id: [$accountId] has no access to ${feature.code} feature.")
            }
            .isDefined()
    }

    override suspend fun <T : Any> canCreate(entityRef: KClass<T>): Boolean {
        return authenticatedAccountProvider.authorities()
            .findCreateScopeFor(entityRef)
            .tapNone {
                val accountId = currentPrincipal().accountId
                logger.warn("Account with id: [$accountId] cannot create ${entityRef.simpleName}.")
            }
            .isDefined()
    }

    override suspend fun <T : Any> canView(entity: T): Boolean {
        return authenticatedAccountProvider.authorities()
            .findViewScopeFor(entity.kClass)
            .tapNone {
                val accountId = currentPrincipal().accountId
                logger.warn("Account with id: [$accountId] cannot view ${entity.kClassSimpleName}.")
            }
            .map { it.predicates.all { it.invoke(entity, currentPrincipal()) } }
            .tap { allPredicatesPassed ->
                if (!allPredicatesPassed) {
                    val accountId = currentPrincipal().accountId
                    logger.warn("Account with id: [$accountId] cannot view that instance of ${entity.kClassSimpleName}.")
                }
            }
            .getOrElse { false }
    }

    override suspend fun <T : Any> canUpdate(entity: T): Boolean {
        return authenticatedAccountProvider.authorities()
            .findUpdateScopeFor(entity.kClass)
            .tapNone {
                val accountId = currentPrincipal().accountId
                logger.warn("Account with id: [$accountId] cannot update ${entity.kClassSimpleName}.")
            }
            .map { it.predicates.any { it.invoke(entity, currentPrincipal()) } }
            .tap { allPredicatesPassed ->
                if (allPredicatesPassed) {
                    val accountId = currentPrincipal().accountId
                    logger.warn("Account with id: [$accountId] cannot update that instance of ${entity.kClassSimpleName}.")
                }
            }
            .getOrElse { false }
    }

    override suspend fun <T : Any> filterCanView(entities: Collection<T>): Collection<T> {
        return entities.filter { canView(it) }
    }

    override suspend fun ensure() = AuthorizedAccountAbilityEnsureProviderImpl(this, authenticatedAccountProvider)

    private suspend fun currentPrincipal() = authenticatedAccountProvider.currentPrincipal()
}
