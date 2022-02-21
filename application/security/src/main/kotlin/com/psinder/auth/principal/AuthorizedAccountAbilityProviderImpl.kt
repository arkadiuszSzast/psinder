package com.psinder.auth.principal

import arrow.core.getOrElse
import arrow.core.toOption
import com.psinder.auth.authority.EntityAccessAuthority
import com.psinder.auth.authority.Feature
import com.psinder.auth.authority.FeatureAccessAuthority
import com.psinder.auth.authority.findCreateScopeFor
import com.psinder.auth.authority.findReadScopeFor
import com.psinder.auth.authority.findUpdateScopeFor
import mu.KotlinLogging
import pl.brightinventions.codified.enums.CodifiedEnum

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

    override suspend fun <T> canCreate(entityRef: Class<T>): Boolean {
        return securityContext.authorities().filterIsInstance<EntityAccessAuthority<T>>().findCreateScopeFor(entityRef)
            .toOption()
            .tapNone {
                val accountId = currentPrincipal().accountId
                val className = entityRef::class.java.name
                logger.warn("Account with id: [$accountId] cannot create $className.")
            }
            .isDefined()
    }

    override suspend fun <T : Any> canView(entity: T): Boolean {
        return securityContext.authorities().filterIsInstance<EntityAccessAuthority<T>>()
            .findReadScopeFor(entity.javaClass)
            .toOption()
            .tapNone {
                val accountId = currentPrincipal().accountId
                val className = entity::class.java.name
                logger.warn("Account with id: [$accountId] cannot view $className.")
            }
            .map { it.predicates.any { it.invoke(entity, currentPrincipal()) } }
            .tap { allPredicatesPassed ->
                if (!allPredicatesPassed) {
                    val accountId = currentPrincipal().accountId
                    val className = entity::class.java.name
                    logger.warn("Account with id: [$accountId] cannot view that instance of $className.")
                }
            }
            .getOrElse { false }
    }

    override suspend fun <T : Any> canUpdate(entity: T): Boolean {
        return securityContext.authorities().filterIsInstance<EntityAccessAuthority<T>>()
            .findUpdateScopeFor(entity.javaClass)
            .toOption()
            .tapNone {
                val accountId = currentPrincipal().accountId
                val className = entity::class.java.name
                logger.warn("Account with id: [$accountId] cannot update $className.")
            }
            .map { it.predicates.any { it.invoke(entity, currentPrincipal()) } }
            .tap { allPredicatesPassed ->
                if (allPredicatesPassed) {
                    val accountId = currentPrincipal().accountId
                    val className = entity::class.java.name
                    logger.warn("Account with id: [$accountId] cannot update that instance of $className.")
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
