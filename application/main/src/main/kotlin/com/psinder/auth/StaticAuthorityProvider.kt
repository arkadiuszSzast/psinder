package com.psinder.auth

import com.psinder.account.AccountProjection
import com.psinder.auth.authority.Authorities
import com.psinder.auth.authority.AuthoritiesProvider
import com.psinder.auth.authority.activatingAccountFeature
import com.psinder.auth.authority.authoritiesFor
import com.psinder.auth.authority.createAccountFeature
import com.psinder.auth.authority.registerDogFeature
import com.psinder.auth.authority.setUserFeatureToggleFeature
import com.psinder.auth.role.Role
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.brightinventions.codified.enums.codifiedEnum

object StaticAuthorityProvider : AuthoritiesProvider {
    override val authorities: Authorities = Authorities.create(
        authoritiesFor(Role.Admin) {
            entityAccess(AccountProjection::class) {
                allScopes()
            }
            featureAccess(setUserFeatureToggleFeature)
        },
        authoritiesFor(Role.User) {
            featureAccess(registerDogFeature)
        },
        authoritiesFor("unknown".codifiedEnum()) {
            featureAccess(createAccountFeature)
            featureAccess(activatingAccountFeature)
        }
    )
}

val authorityProviderModule = module {
    single { StaticAuthorityProvider } bind AuthoritiesProvider::class
}
