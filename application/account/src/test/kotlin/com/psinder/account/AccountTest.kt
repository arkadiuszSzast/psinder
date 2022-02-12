package com.psinder.account

import com.psinder.test.utils.faker
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class AccountTest : DescribeSpec({

    describe("create account") {

        it("should have staged status") {
            val account = Account.create(
                faker.accountModule.emailAddress(),
                faker.accountModule.personalData(),
                faker.accountModule.hashedPassword(),
                faker.accountModule.timeZone()
            )

            expectThat(account)
                .get { status.knownOrNull() }
                .isEqualTo(AccountStatus.Staged)
        }
    }
})
