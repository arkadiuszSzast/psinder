package com.psinder.account

import com.psinder.test.utils.faker
import io.kotest.core.spec.style.DescribeSpec
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class AccountTest : DescribeSpec({

    describe("create account") {

        it("should have staged status") {
            // arrange && act
            val accountAggregate = AccountAggregate.create(
                faker.accountModule.emailAddress(),
                faker.accountModule.personalData(),
                faker.accountModule.role().codifiedEnum(),
                faker.accountModule.hashedPassword(),
                faker.accountModule.timeZone()
            )

            // assert
            expectThat(accountAggregate)
                .get { status.knownOrNull() }
                .isEqualTo(AccountStatus.Staged)
        }
    }
})
