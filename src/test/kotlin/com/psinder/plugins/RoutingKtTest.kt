package com.psinder.plugins

import com.psinder.account.configureAccountRouting
import com.psinder.utils.mockConfigureAccountRouting
import io.kotest.core.spec.style.DescribeSpec
import io.ktor.server.testing.*
import io.mockk.verify

class RoutingKtTest : DescribeSpec({

    describe("check registered routes") {

        it("verify routes") {
            withTestApplication {
                mockConfigureAccountRouting()

                application.configureRouting()

                verify(exactly = 1) { application.configureAccountRouting() }
            }
        }
    }
})
