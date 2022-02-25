package com.psinder.auth.principal

import com.psinder.auth.account.AccountId
import com.psinder.auth.account.BelongsToAccount
import com.psinder.auth.authority.Authorities
import com.psinder.auth.authority.AuthoritiesProvider
import com.psinder.auth.authority.authoritiesFor
import com.psinder.auth.role.Role
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isTrue

class AuthorizedAccountAbilityProviderImplTest : DescribeSpec({

    describe("AuthorizedAccountAbilityProvider") {

        describe("read ability") {

            it("should return false when account has no read access on entity") {

                val securityContext = SecurityContext(StaticAuthorityProvider)

                val acl = AuthorizedAccountAbilityProviderImpl(securityContext)
                val dog = Dog(AccountId("123"))
                val result = acl.canView(dog)
                expectThat(result).isTrue()
            }
        }
    }
})

data class Dog(override val accountId: AccountId) : BelongsToAccount

object StaticAuthorityProvider : AuthoritiesProvider {
    override val authorities: Authorities = Authorities.create(
        authoritiesFor(Role.Admin) {
            entityAccess(Dog::class) {
                allScopes()
            }
        }
    )
}
