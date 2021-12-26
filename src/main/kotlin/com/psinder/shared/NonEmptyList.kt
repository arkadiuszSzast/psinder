package com.psinder.shared

import arrow.core.NonEmptyList
import arrow.typeclasses.Semigroup

internal fun <A> NonEmptyList<A>.reduce(semigroup: Semigroup<A>) = semigroup.run {
    this@reduce.reduce { acc, next ->
        acc.combine(next)
    }
}

internal fun <A, B> NonEmptyList<A>.reduceMap(semigroup: Semigroup<B>, f: (A) -> B) = semigroup.run {
    this@reduceMap.map { f(it) }.reduce { acc, next ->
        acc.combine(next)
    }
}
