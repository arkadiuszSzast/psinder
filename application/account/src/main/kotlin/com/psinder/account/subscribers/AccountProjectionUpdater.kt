package com.psinder.account.subscribers

import com.psinder.account.AccountCreatedEvent
import com.psinder.events.EventFamily
import com.psinder.events.getAs
import com.psinder.events.subscribeByFamily
import io.ktor.application.Application
import io.traxter.eventstoredb.EventStoreDB
import kotlinx.coroutines.launch

internal fun Application.accountProjectionUpdater(db: EventStoreDB) = launch {
    db.subscribeByFamily(EventFamily.Account) {
        println(event.getAs<AccountCreatedEvent>())
    }
}
