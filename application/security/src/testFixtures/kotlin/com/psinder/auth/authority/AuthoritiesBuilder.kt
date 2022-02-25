package com.psinder.auth.authority

fun authorities(customize: AuthoritiesListBuilder.() -> Unit) = AuthoritiesListBuilder().apply(customize).build()
