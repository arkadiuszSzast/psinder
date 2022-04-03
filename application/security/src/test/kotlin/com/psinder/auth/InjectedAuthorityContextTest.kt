package com.psinder.auth

import com.psinder.auth.account.AccountId
import com.psinder.auth.authority.authorities
import com.psinder.auth.authority.withInjectedAuthorities
import com.psinder.auth.principal.AuthorizedAccountAbilityEnsureProviderImpl
import com.psinder.auth.principal.AuthorizedAccountAbilityProviderImpl
import com.psinder.auth.principal.fakeAuthenticatedAccountProvider
import com.psinder.auth.principal.unauthenticatedAccountProvider
import com.psinder.auth.role.Role
import com.psinder.support.Dog
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isSuccess
import strikt.assertions.isTrue

class InjectedAuthorityContextTest : DescribeSpec({

    describe("can inject authorities") {

        it("works with AuthorizedAccountAbilityProvider") {
            val provider = unauthenticatedAccountProvider { }

            val acl = AuthorizedAccountAbilityProviderImpl(provider)
            val dog = Dog(AccountId("000"))
            val authoritiesToInject = authorities {
                entityAccess(Dog::class) { viewScope() }
            }

            withInjectedAuthorities(authoritiesToInject) {
                val result = acl.canView(dog)

                expectThat(result).isTrue()
            }
        }

        it("injected authorities are checked before original ones in AuthorizedAccountAbilityProvider") {
            val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                entityAccess(Dog::class) { viewScope() }
            }

            val acl = AuthorizedAccountAbilityProviderImpl(provider)
            val authoritiesToInject = authorities {
                entityAccess(Dog::class) { createScope() }
            }

            withInjectedAuthorities(authoritiesToInject) {
                val result = acl.canCreate(Dog::class)

                expectThat(result).isTrue()
            }
        }
    }

    it("works with AuthorizedAccountAbilityEnsureProvider") {
        val provider = unauthenticatedAccountProvider { }

        val acl = AuthorizedAccountAbilityProviderImpl(provider)
        val dog = Dog(AccountId("000"))
        val authoritiesToInject = authorities {
            entityAccess(Dog::class) { viewScope() }
        }

        withInjectedAuthorities(authoritiesToInject) {
            expectThat(runCatching { acl.ensure().canView(dog) })
                .isSuccess()
        }
    }

    it("injected authorities are checked before original ones in AuthorizedAccountAbilityEnsureProvider") {
        val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
            entityAccess(Dog::class) { viewScope() }
        }

        val acl = AuthorizedAccountAbilityProviderImpl(provider)
        val authoritiesToInject = authorities {
            entityAccess(Dog::class) { createScope() }
        }

        withInjectedAuthorities(authoritiesToInject) {
            expectThat(runCatching { acl.ensure().canCreate(Dog::class) })
                .isSuccess()
        }
    }

})
