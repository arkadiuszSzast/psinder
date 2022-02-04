package com.psinder.kediatr

import com.trendyol.kediatr.Command
import com.trendyol.kediatr.CommandWithResult
import com.trendyol.kediatr.Notification
import com.trendyol.kediatr.Query
import pl.brightinventions.codified.enums.codifiedEnum

internal object KediatrRequestTypeExtractor {
    fun <TRequest> extract(request: TRequest) =
        when (request) {
            is Command -> KediatrRequestType.Command.codifiedEnum()
            is CommandWithResult<*> -> KediatrRequestType.CommandWithResult.codifiedEnum()
            is Query<*> -> KediatrRequestType.Query.codifiedEnum()
            is Notification -> KediatrRequestType.Notification.codifiedEnum()
            else -> "unknown".codifiedEnum()
        }
}
