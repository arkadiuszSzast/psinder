package com.psinder.account.config

import com.psinder.mail.MailProperties
import com.psinder.mail.MailSubject
import com.psinder.mail.MailTemplateId
import com.psinder.shared.EmailAddress
import io.kotest.core.spec.style.DescribeSpec
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class MailConfigTest : DescribeSpec({

    describe("get mail config test") {

        it("get properties") {
            expectThat(MailConfig) {
                get { activateAccount }.isEqualTo(
                    MailProperties(
                        MailTemplateId("activate-account-template-id"),
                        MailSubject("Activate Your Account"),
                        EmailAddress.create("activate-account-sender@mail.com")
                    )
                )
            }
        }
    }
})
