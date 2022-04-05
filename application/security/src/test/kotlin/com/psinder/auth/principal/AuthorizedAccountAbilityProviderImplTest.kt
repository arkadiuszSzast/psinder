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
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))

                // act
                val result = acl.canView(dog)

                // assert
                expectThat(result).isA<Deny>()
            }

            it("should return false when account has permissions only on different entity") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { viewScope() }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))

                // act
                val result = acl.canView(dog)

                // assert
                expectThat(result).isA<Deny>()
            }

            it("should return false when has permission but not meet additional predicate") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope(ownedPredicate()) }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))

                // act
                val result = acl.canView(dog)

                // assert
                expectThat(result).isA<Deny>()
            }

            it("should pass when has access") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope() }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))

                // act
                val result = acl.canView(dog)

                // assert
                expectThat(result).isA<Allow>()
            }

            it("should pass when has access and is owner") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope(ownedPredicate()) }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("123"))

                // act
                val result = acl.canView(dog)

                // assert
                expectThat(result).isA<Allow>()
            }
        }

        describe("update ability") {

            it("should return false when account has create permissions but no update") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))

                // act
                val result = acl.canUpdate(dog)

                // assert
                expectThat(result).isA<Deny>()
            }

            it("should return false when account has permissions only on different entity") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { updateScope() }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))

                // act
                val result = acl.canUpdate(dog)

                // assert
                expectThat(result).isA<Deny>()
            }

            it("should return false when has permission but not meet additional predicate") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope(ownedPredicate()) }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))

                // act
                val result = acl.canUpdate(dog)

                // assert
                expectThat(result).isA<Deny>()
            }

            it("should pass when has access") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope() }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("000"))

                // act
                val result = acl.canUpdate(dog)

                // assert
                expectThat(result).isA<Allow>()
            }

            it("should pass when has access and is owner") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope(ownedPredicate()) }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val dog = Dog(AccountId("123"))

                // act
                val result = acl.canUpdate(dog)

                // assert
                expectThat(result).isA<Allow>()
            }
        }

        describe("create ability") {

            it("should return false when account has view permissions but no create") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope() }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)

                // act
                val result = acl.canCreate(Dog::class)

                // assert
                expectThat(result).isA<Deny>()
            }

            it("should return false when account has permissions only on different entity") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { createScope() }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)

                // act
                val result = acl.canCreate(Dog::class)

                // assert
                expectThat(result).isA<Deny>()
            }

            it("should pass when has access") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)

                // act
                val result = acl.canCreate(Dog::class)

                // assert
                expectThat(result).isA<Allow>()
            }
        }

        describe("feature ability") {

            it("should return true when feature found") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    featureAccess(Feature("feature_a"))
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)

                // act
                val result = acl.hasAccessTo(Feature("feature_a"))

                // assert
                expectThat(result).isA<Allow>()
            }

            it("should return false when feature not found") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    featureAccess(Feature("feature_a"))
                }
                val acl = AuthorizedAccountAbilityProviderImpl(provider)

                // act
                val result = acl.hasAccessTo(Feature("feature_b"))

                // assert
                expectThat(result).isA<Deny>()
            }
        }
    }
})
