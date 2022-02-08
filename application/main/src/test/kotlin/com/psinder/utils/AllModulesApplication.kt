package com.psinder.utils

import com.psinder.account.accountModule
import com.psinder.events.eventStoreModule
import com.psinder.main
import com.psinder.monitoring.monitoringModule
import io.ktor.application.Application

internal fun Application.allModules() {
    main()
    monitoringModule()
    eventStoreModule()
    accountModule()
}