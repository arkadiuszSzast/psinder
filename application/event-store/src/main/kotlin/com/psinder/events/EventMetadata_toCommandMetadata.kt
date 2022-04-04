package com.psinder.events

import com.trendyol.kediatr.CommandMetadata

fun EventMetadata.toCommandMetadata() =
    CommandMetadata(this.correlationId?.correlationId, this.causationId?.causationId)
