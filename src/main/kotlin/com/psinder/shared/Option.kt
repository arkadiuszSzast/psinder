package com.psinder.shared

import arrow.core.Option

internal fun <T> List<Option<T>>.allNotEmpty(): List<T> = this.mapNotNull { it.orNull() }
