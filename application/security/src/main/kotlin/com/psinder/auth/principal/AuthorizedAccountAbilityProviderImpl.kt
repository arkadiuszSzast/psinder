package com.psinder.auth.principal

import arrow.core.getOrElse
import arrow.core.toOption
import com.psinder.auth.authority.EntityAccessAuthority
import com.psinder.auth.authority.Feature
import com.psinder.auth.authority.FeatureAccessAuthority
import com.psinder.auth.authority.findCreateScopeFor
import com.psinder.auth.authority.findReadScopeFor
import com.psinder.auth.authority.findUpdateScopeFor
import com.psinder.shared.kClass
import com.psinder.shared.kClassSimpleName
import mu.KotlinLogging
import pl.brightinventions.codified.enums.CodifiedEnum
import kotlin.reflect.KClass

class AuthorizedAccountAbilityProviderImpl(
    private val securityContext: SecurityContext
) : AuthorizedAccountAbilityProvider {
    private val logger = KotlinLogging.logger {}

    override suspend fun hasAccessTo(feature: CodifiedEnum<Feature, String>): Boolean {
        return securityContext.authorities().filterIsInstance<FeatureAccessAuthority>()
            .find { it.feature.code() == feature.code() }
            .toOption()
            .tapNone {
                val accountId = currentPrincipal().accountId
                logger.warn("Account with id: [$accountId] has no access to ${feature.code()} feature.")
            }
            .isDefined()
    }

    override suspend fun <T : Any> canCreate(entityRef: KClass<T>): Boolean {
        return securityContext.authorities().filterIsInstance<EntityAccessAuthority<T>>().findCreateScopeFor(entityRef)
            .toOption()
            .tapNone {
                val accountId = currentPrincipal().accountId
                logger.warn("Account with id: [$accountId] cannot create ${entityRef.simpleName}.")
            }
            .isDefined()
    }

    override suspend fun <T : Any> canView(entity: T): Boolean {
        return securityContext.authorities().filterIsInstance<EntityAccessAuthority<T>>()
            .findReadScopeFor(entity.kClass)
            .toOption()
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
        return securityContext.authorities().filterIsInstance<EntityAccessAuthority<T>>()
            .findUpdateScopeFor(entity.kClass)
            .toOption()
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

    override suspend fun ensure() = AuthorizedAccountAbilityEnsureProviderImpl(this, securityContext)

    private suspend fun currentPrincipal() = securityContext.currentPrincipal()
}
