package com.psinder.shared.validation

import arrow.core.Option
import com.psinder.shared.allNotEmpty

fun interface ValidationRule<T> {
    fun check(target: T): Option<ValidationError>
}

fun <T> Collection<ValidationRule<T>>.checkAll(target: T)  = this.map { it.check(target) }.allNotEmpty()