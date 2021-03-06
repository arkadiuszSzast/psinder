package com.psinder.auth.principal

import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.toOption
import com.psinder.auth.AuthorityCheckException
import com.psinder.auth.InjectedAuthorityContext
import com.psinder.auth.authority.AccountAuthorities
import com.psinder.auth.authority.Allow
import com.psinder.auth.authority.AuthorityLevel
import com.psinder.auth.authority.AuthorityScope
import com.psinder.auth.authority.Decision
import com.psinder.auth.authority.Deny
import com.psinder.auth.authority.Feature
import com.psinder.shared.kClass
import com.psinder.shared.kClassSimpleName
import kotlinx.coroutines.currentCoroutineContext
import mu.KotlinLogging
import java.util.UUID
import kotlin.reflect.KClass

class AuthorizedAccountAbilityProviderImpl(
    private val authenticatedAccountProvider: AuthenticatedAccountProvider
) : AuthorizedAccountAbilityProvider {
    private val logger = KotlinLogging.logger {}

    override suspend fun hasAccessTo(feature: Feature): Decision {
        return getAuthorities()
            .findFeature(feature)
            .fold(
                {
                    val accountId = currentPrincipal().accountId
                    logger.warn("Account with id: [$accountId] has no access to ${feature.code} feature.")
                    Deny(AuthorityCheckException("Account with id: [$accountId] has no access to ${feature.code} feature"))
                },
                {
                    Allow(UUID.randomUUID())
                }
            )
    }

    override suspend fun <T : Any> canCreate(entityRef: KClass<T>): Decision {
        return getAuthorities()
            .findCreateScopeFor(entityRef)
            .fold(
                {
                    val accountId = currentPrincipal().accountId
                    logger.warn("Account with id: [$accountId] cannot create ${entityRef.simpleName}.")
                    Deny(AuthorityCheckException("Account with id: [$accountId] cannot create ${entityRef.simpleName}"))
                },
                {
                    Allow(UUID.randomUUID())
                }
            )
    }

    override suspend fun <T : Any> canView(entity: T): Decision {
        return getAuthorities()
            .findViewScopeFor(entity.kClass)
            .check(entity, AuthorityLevel.View)
    }

    override suspend fun <T : Any> canUpdate(entity: T): Decision {
        return getAuthorities()
            .findUpdateScopeFor(entity.kClass)
            .check(entity, AuthorityLevel.Update)
    }

    override suspend fun <T : Any> filterCanView(entities: Collection<T>): Collection<T> {
        return entities.filter { canView(it).toBoolean() }
    }

    override suspend fun ensure() = AuthorizedAccountAbilityEnsureProviderImpl(this)

    private suspend fun currentPrincipal() = authenticatedAccountProvider.currentPrincipal()

    private suspend fun getAuthorities() = currentCoroutineContext()[InjectedAuthorityContext].toOption()
        .map { AccountAuthorities(it.authorities) }
        .getOrElse { authenticatedAccountProvider.authorities() }

    private suspend fun <T : Any> Option<AuthorityScope<T>>.check(entity: T, authorityLevel: AuthorityLevel) =
        fold(
            {
                val accountId = currentPrincipal().accountId
                logger.warn("Account with id: [$accountId] cannot ${authorityLevel.code} ${entity.kClassSimpleName}.")
                Deny(AuthorityCheckException("Account with id: [$accountId] cannot ${authorityLevel.code} ${entity.kClassSimpleName}."))
            },
            {
                val allPredicatesPassed = it.predicates.all { it.invoke(entity, currentPrincipal()) }
                if (!allPredicatesPassed) {
                    val accountId = currentPrincipal().accountId
                    logger.warn("Account with id: [$accountId] cannot ${authorityLevel.code} that instance of ${entity.kClassSimpleName}.")
                    Deny(
                        AuthorityCheckException(
                            "Account with id: [$accountId] cannot ${authorityLevel.code} that instance of ${entity.kClassSimpleName}."
                        )
                    )
                } else {
                    Allow(UUID.randomUUID())
                }
            }
        )
}
