package com.psinder.auth

import com.psinder.account.AccountProjection
import com.psinder.auth.authority.Authorities
import com.psinder.auth.authority.AuthoritiesProvider
import com.psinder.auth.authority.activatingAccountFeature
import com.psinder.auth.authority.authoritiesFor
import com.psinder.auth.authority.createAccountFeature
import com.psinder.auth.authority.impersonateDogFeature
import com.psinder.auth.authority.ownedPredicate
import com.psinder.auth.authority.registerDogFeature
import com.psinder.auth.authority.setUserFeatureToggleFeature
import com.psinder.auth.role.Role
import com.psinder.dog.DogOverviewProjection
import com.psinder.dog.DogProfileProjection
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.brightinventions.codified.enums.codifiedEnum

object StaticAuthorityProvider : AuthoritiesProvider {
    override val authorities: Authorities = Authorities.create(
        authoritiesFor(Role.Admin) {
            featureAccess(setUserFeatureToggleFeature)
            featureAccess(impersonateDogFeature)
            entityAccess(AccountProjection::class) {
                allScopes()
            }
            entityAccess(DogProfileProjection::class) {
                allScopes()
            }
            entityAccess(DogOverviewProjection::class) {
                allScopes()
            }
        },
        authoritiesFor(Role.User) {
            featureAccess(registerDogFeature)
            featureAccess(impersonateDogFeature)
            entityAccess(DogOverviewProjection::class) {
                viewScope()
            }
            entityAccess(DogProfileProjection::class) {
                allScopes(ownedPredicate())
            }
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
