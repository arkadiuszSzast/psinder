package com.psinder.account.subscribers

import com.psinder.database.EventStoreTest
import io.ktor.server.testing.withTestApplication
import io.mockk.mockk

class AccountProjectionUpdaterKtTest : EventStoreTest() {
    init {
        describe("account projection updater") {

            it("create account on account-created event") {
                withTestApplication {
                    application.accountProjectionUpdater(eventStoreDb, mockk())
                }
            }
        }
    }
}
