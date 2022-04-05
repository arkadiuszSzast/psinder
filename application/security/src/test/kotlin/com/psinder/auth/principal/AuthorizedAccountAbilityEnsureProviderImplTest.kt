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

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                expectThrows<AuthorityCheckException> {
                    aclEnsure.canView(dog)
                }
            }

            it("should return false when account has permissions only on different entity") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { viewScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                expectThrows<AuthorityCheckException> {
                    aclEnsure.canView(dog)
                }
            }

            it("should return false when has permission but not meet additional predicate") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                expectThrows<AuthorityCheckException> {
                    aclEnsure.canView(dog)
                }
            }

            it("should pass when has access") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                expectCatching { aclEnsure.canView(dog) }
                    .isSuccess()
            }

            it("should pass when has access and is owner") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("123"))

                expectCatching { aclEnsure.canView(dog) }
                    .isSuccess()
            }
        }

        describe("update ability") {

            it("should return false when account has create permissions but no update") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                expectThrows<AuthorityCheckException> {
                    aclEnsure.canUpdate(dog)
                }
            }

            it("should return false when account has permissions only on different entity") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { updateScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                expectThrows<AuthorityCheckException> {
                    aclEnsure.canUpdate(dog)
                }
            }

            it("should return false when has permission but not meet additional predicate") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                expectThrows<AuthorityCheckException> {
                    aclEnsure.canUpdate(dog)
                }
            }

            it("should pass when has access") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("000"))

                expectCatching { aclEnsure.canUpdate(dog) }
                    .isSuccess()
            }

            it("should pass when has access and is owner") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { updateScope(ownedPredicate()) }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)
                val dog = Dog(AccountId("123"))

                expectCatching { aclEnsure.canUpdate(dog) }
                    .isSuccess()
            }
        }

        describe("create ability") {

            it("should return false when account has view permissions but no create") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { viewScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)

                expectThrows<AuthorityCheckException> {
                    aclEnsure.canCreate(Dog::class)
                }
            }

            it("should return false when account has permissions only on different entity") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Cat::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)

                expectThrows<AuthorityCheckException> {
                    aclEnsure.canCreate(Dog::class)
                }
            }

            it("should pass when has access") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    entityAccess(Dog::class) { createScope() }
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)

                expectCatching { aclEnsure.canCreate(Dog::class) }
                    .isSuccess()
            }
        }

        describe("feature ability") {

            it("should return true when feature found") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    featureAccess(Feature("feature_a"))
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)

                expectCatching { aclEnsure.hasAccessTo(Feature("feature_a")) }
                    .isSuccess()
            }

            it("should return false when feature not found") {

                val provider = fakeAuthenticatedAccountProvider(AccountId("123"), Role.User) {
                    featureAccess(Feature("feature_a"))
                }

                val acl = AuthorizedAccountAbilityProviderImpl(provider)
                val aclEnsure = AuthorizedAccountAbilityEnsureProviderImpl(acl)

                expectThrows<AuthorityCheckException> {
                    aclEnsure.hasAccessTo(Feature("feature_b"))
                }
            }
        }
    }
})
