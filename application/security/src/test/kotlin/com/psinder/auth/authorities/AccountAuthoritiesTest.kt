package com.psinder.auth.authorities

import com.psinder.auth.authority.AccountAuthorities
import com.psinder.auth.authority.Feature
import com.psinder.auth.authority.authorities
import com.psinder.support.Cat
import com.psinder.support.Dog
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.arrow.isNone
import strikt.arrow.isSome

class AccountAuthoritiesTest : DescribeSpec({

    describe("entity access abilities") {

        it("can get view ability") {
            val authorities = authorities {
                entityAccess(Dog::class) { viewScope() }
            }.let { AccountAuthorities(it) }

            expectThat(authorities.findViewScopeFor(Dog::class))
                .isSome()
        }

        it("can get create ability") {
            val authorities = authorities {
                entityAccess(Dog::class) { createScope() }
            }.let { AccountAuthorities(it) }

            expectThat(authorities.findCreateScopeFor(Dog::class))
                .isSome()
        }

        it("can get update ability") {
            val authorities = authorities {
                entityAccess(Dog::class) { updateScope() }
            }.let { AccountAuthorities(it) }

            expectThat(authorities.findUpdateScopeFor(Dog::class))
                .isSome()
        }

        it("is none when ability not found") {
            val authorities = authorities {
                entityAccess(Dog::class) { viewScope(); updateScope(); }
            }.let { AccountAuthorities(it) }

            expectThat(authorities.findCreateScopeFor(Dog::class))
                .isNone()
        }

        it("pick ability for matching entity") {
            val authorities = authorities {
                entityAccess(Dog::class) { viewScope() }
                entityAccess(Cat::class) { updateScope() }
            }.let { AccountAuthorities(it) }

            expectThat(authorities.findViewScopeFor(Cat::class))
                .isNone()
            expectThat(authorities.findViewScopeFor(Dog::class))
                .isSome()
        }

        it("when authority is duplicated should pick first one") {
            val authorities = authorities {
                entityAccess(Dog::class) { viewScope() }
                entityAccess(Dog::class) { updateScope() }
            }.let { AccountAuthorities(it) }

            expectThat(authorities.findViewScopeFor(Dog::class))
                .isSome()
            expectThat(authorities.findUpdateScopeFor(Dog::class))
                .isNone()
        }
    }

    describe("features abilities") {

        it("should find matching ability") {
            val authorities = authorities {
                featureAccess(Feature("feature_a"))
            }.let { AccountAuthorities(it) }

            expectThat(authorities.findFeature(Feature("feature_a")))
                .isSome()
        }

        it("should not find ability") {
            val authorities = authorities {
                featureAccess(Feature("feature_a"))
            }.let { AccountAuthorities(it) }

            expectThat(authorities.findFeature(Feature("feature_b")))
                .isNone()
        }
    }
})
