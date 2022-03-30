package com.psinder.auth.principal

import arrow.core.getOrElse
import arrow.core.toOption
import com.psinder.auth.InjectedAuthorityContext
import com.psinder.auth.authority.AccountAuthorities
import com.psinder.auth.authority.Feature
import com.psinder.shared.kClass
import com.psinder.shared.kClassSimpleName
import kotlinx.coroutines.currentCoroutineContext
import mu.KotlinLogging
import kotlin.reflect.KClass

class AuthorizedAccountAbilityProviderImpl(
    private val authenticatedAccountProvider: AuthenticatedAccountProvider
) : AuthorizedAccountAbilityProvider {
    private val logger = KotlinLogging.logger {}

    override suspend fun hasAccessTo(feature: Feature): Boolean {
        return getAuthorities()
            .findFeature(feature)
            .tapNone {
                val accountId = currentPrincipal().accountId
                logger.warn("Account with id: [$accountId] has no access to ${feature.code} feature.")
            }
            .isDefined()
    }

    override suspend fun <T : Any> canCreate(entityRef: KClass<T>): Boolean {
        return getAuthorities()
            .findCreateScopeFor(entityRef)
            .tapNone {
                val accountId = currentPrincipal().accountId
                logger.warn("Account with id: [$accountId] cannot create ${entityRef.simpleName}.")
            }
            .isDefined()
    }

    override suspend fun <T : Any> canView(entity: T): Boolean {
        return getAuthorities()
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
        return getAuthorities()
            .findUpdateScopeFor(entity.kClass)
            .tapNone {
                val accountId = currentPrincipal().accountId
                logger.warn("Account with id: [$accountId] cannot update ${entity.kClassSimpleName}.")
            }
            .map { it.predicates.all { it.invoke(entity, currentPrincipal()) } }
            .tap { allPredicatesPassed ->
                if (!allPredicatesPassed) {
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

    private suspend fun getAuthorities() = currentCoroutineContext()[InjectedAuthorityContext].toOption()
        .map { AccountAuthorities(it.authorities) }
        .getOrElse { authenticatedAccountProvider.authorities() }
}
