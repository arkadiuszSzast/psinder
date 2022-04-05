package com.psinder.auth.principal

import com.psinder.auth.AuthorityCheckException
import com.psinder.auth.account.AccountId
import com.psinder.auth.authority.Feature
import com.psinder.auth.authority.ownedPredicate
import com.psinder.auth.role.Role
import com.psinder.support.Cat
import com.psinder.support.Dog
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectCatching
import strikt.api.expectThrows
import strikt.assertions.isSuccess

class AuthorizedAccountAbilityEnsureProviderImplTest : DescribeSpec({

    describe("AuthorizedAccountAbilityProvider") {

        describe("read ability") {

            it("should return false when account has create permissions but no read") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                // act && assert
                expectThrows<AuthorityCheckException> {
                    aclEnsure.canView(dog)
                }
            }

            it("should return false when account has permissions only on different entity") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { viewScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                // act && assert
                expectThrows<AuthorityCheckException> {
                    aclEnsure.canView(dog)
                }
            }

            it("should return false when has permission but not meet additional predicate") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                // act && assert
                expectThrows<AuthorityCheckException> {
                    aclEnsure.canView(dog)
                }
            }

            it("should pass when has access") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                // act && assert
                expectCatching { aclEnsure.canView(dog) }
                    .isSuccess()
            }

            it("should pass when has access and is owner") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("123"))

                // act && assert
                expectCatching { aclEnsure.canView(dog) }
                    .isSuccess()
            }
        }

        describe("update ability") {
            // arrange
            it("should return false when account has create permissions but no update") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                // act && assert
                expectThrows<AuthorityCheckException> {
                    aclEnsure.canUpdate(dog)
                }
            }

            it("should return false when account has permissions only on different entity") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { updateScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                // act && assert
                expectThrows<AuthorityCheckException> {
                    aclEnsure.canUpdate(dog)
                }
            }

            it("should return false when has permission but not meet additional predicate") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                // act && assert
                expectThrows<AuthorityCheckException> {
                    aclEnsure.canUpdate(dog)
                }
            }

            it("should pass when has access") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                // act && assert
                expectCatching { aclEnsure.canUpdate(dog) }
                    .isSuccess()
            }

            it("should pass when has access and is owner") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("123"))

                // act && assert
                expectCatching { aclEnsure.canUpdate(dog) }
                    .isSuccess()
            }
        }

        describe("create ability") {

            it("should return false when account has view permissions but no create") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)

                // act && assert
                expectThrows<AuthorityCheckException> {
                    aclEnsure.canCreate(Dog::class)
                }
            }

            it("should return false when account has permissions only on different entity") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)

                // act && assert
                expectThrows<AuthorityCheckException> {
                    aclEnsure.canCreate(Dog::class)
                }
            }

            it("should pass when has access") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)

                // act && assert
                expectCatching { aclEnsure.canCreate(Dog::class) }
                    .isSuccess()
            }
        }

        describe("feature ability") {

            it("should return true when feature found") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    featureAccess(Feature("feature_a"))
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)

                // act && assert
                expectCatching { aclEnsure.hasAccessTo(Feature("feature_a")) }
                    .isSuccess()
            }

            it("should return false when feature not found") {
                // arrange
                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    featureAccess(Feature("feature_a"))
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)

                // act && assert
                expectThrows<AuthorityCheckException> {
                    aclEnsure.hasAccessTo(Feature("feature_b"))
                }
            }
        }
    }
})
