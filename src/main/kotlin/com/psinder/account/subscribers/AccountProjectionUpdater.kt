package com.psinder.account.subscribers

import com.psinder.account.AccountCreatedEvent
import com.psinder.shared.events.getAs
import io.ktor.application.Application
import io.traxter.eventstoredb.EventStoreDB
import kotlinx.coroutines.launch

internal fun Application.accountProjectionUpdater(db: EventStoreDB) = launch {
    db.subscribeToStream(AccountCreatedEvent.eventFamily.code) {
        event.getAs<AccountCreatedEvent>()
    }
}
