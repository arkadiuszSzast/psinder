package com.psinder.account.activation.subscribers

import com.psinder.account.accountAggregateType
import com.psinder.events.streamName
import io.ktor.application.Application
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.StreamGroup
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.launch

internal fun Application.accountActivationTokensProjectionSubscriber(
    eventStoreDb: EventStoreDB,
    accountActivationTokensProjectionUpdater: AccountActivationTokensProjectionUpdater
) = launch {
    eventStoreDb.subscribeToPersistedStream(
        StreamName(accountAggregateType.streamName),
        StreamGroup("account-activation-tokens-projection-updater")
    ) { _, event ->
        accountActivationTokensProjectionUpdater.update(event.event)
    }
}
