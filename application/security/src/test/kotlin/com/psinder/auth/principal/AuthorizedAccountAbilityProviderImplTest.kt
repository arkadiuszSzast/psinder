package com.psinder.auth.principal

import com.psinder.auth.account.AccountId
import com.psinder.auth.authority.Allow
import com.psinder.auth.authority.Deny
import com.psinder.auth.authority.Feature
import com.psinder.auth.authority.ownedPredicate
import com.psinder.auth.role.Role
import com.psinder.support.Cat
import com.psinder.support.Dog
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isA

class AuthorizedAccountAbilityProviderImplTest : DescribeSpec({

    describe("AuthorizedAccountAbilityProvider") {

        describe("read ability") {

            it("should return false when account has create permissions but no read") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))
                val result = acl.canView(dog)

                expectThat(result).isA<Deny>()
            }

            it("should return false when account has permissions only on different entity") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { viewScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))
                val result = acl.canView(dog)

                expectThat(result).isA<Deny>()
            }

            it("should return false when has permission but not meet additional predicate") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))
                val result = acl.canView(dog)

                expectThat(result).isA<Deny>()
            }

            it("should pass when has access") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))
                val result = acl.canView(dog)

                expectThat(result).isA<Allow>()
            }

            it("should pass when has access and is owner") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("123"))
                val result = acl.canView(dog)

                expectThat(result).isA<Allow>()
            }
        }

        describe("update ability") {

            it("should return false when account has create permissions but no update") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))
                val result = acl.canUpdate(dog)

                expectThat(result).isA<Deny>()
            }

            it("should return false when account has permissions only on different entity") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { updateScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))
                val result = acl.canUpdate(dog)

                expectThat(result).isA<Deny>()
            }

            it("should return false when has permission but not meet additional predicate") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))
                val result = acl.canUpdate(dog)

                expectThat(result).isA<Deny>()
            }

            it("should pass when has access") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))
                val result = acl.canUpdate(dog)

                expectThat(result).isA<Allow>()
            }

            it("should pass when has access and is owner") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("123"))
                val result = acl.canUpdate(dog)

                expectThat(result).isA<Allow>()
            }
        }

        describe("create ability") {

            it("should return false when account has view permissions but no create") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val result = acl.canCreate(Dog::class)

                expectThat(result).isA<Deny>()
            }

            it("should return false when account has permissions only on different entity") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val result = acl.canCreate(Dog::class)

                expectThat(result).isA<Deny>()
            }

            it("should pass when has access") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val result = acl.canCreate(Dog::class)

                expectThat(result).isA<Allow>()
            }
        }

        describe("feature ability") {

            it("should return true when feature found") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    featureAccess(Feature("feature_a"))
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val result = acl.hasAccessTo(Feature("feature_a"))

                expectThat(result).isA<Allow>()
            }

            it("should return false when feature not found") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    featureAccess(Feature("feature_a"))
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val result = acl.hasAccessTo(Feature("feature_b"))

                expectThat(result).isA<Deny>()
            }
        }
    }
})
