package com.psinder.auth

class AuthorizedAccountAbilityProviderImpl(
    private val authorityProvider: AuthoritiesProvider,
    private val securityContext: SecurityContext
) :
    AuthorizedAccountAbilityProvider {

    override suspend fun <T> canCreate(entityRef: Class<T>) {
        verifyHasMatchingAuthority(entityRef, AuthorityLevel.Create)
    }

    override suspend fun <T : Any> canView(entity: T) {
        verifyHasMatchingAuthority(entity::class.java, AuthorityLevel.Read)
        if (entity is BelongsToAccount && entity.accountId != currentPrincipal().accountId) {
            throw SecurityException("Account with id: [${currentPrincipal().accountId}] cannot view ${entity::class.java.name} that belongs to other account")
        }
    }

    override suspend fun <T : Any> canUpdate(entity: T) {
        verifyHasMatchingAuthority(entity::class.java, AuthorityLevel.Update)
        if (entity is BelongsToAccount && entity.accountId != currentPrincipal().accountId) {
            throw SecurityException("Account with id: [${currentPrincipal().accountId}] cannot update ${entity::class.java.name} that belongs to other account")
        }
    }

    private suspend fun currentPrincipal() = securityContext.currentPrincipal()
    private suspend fun authorities() = authorityProvider.authorities[currentPrincipal().role] ?: emptyList()

    private suspend fun <T> verifyHasMatchingAuthority(entityRef: Class<T>, level: AuthorityLevel) =
        authorities().find { it.entityRef == entityRef }?.levels?.any { it == level }
            ?: throw SecurityException("Account with id: [${currentPrincipal().accountId}] cannot ${level.name} ${entityRef.name}")
}
