package com.psinder.auth.authority

import com.psinder.auth.InjectedAuthorityContext
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext

suspend fun withInjectedAuthority(features: List<Feature>, block: suspend () -> Unit) {
    val authorities = features.map { FeatureAccessAuthority(it) }
    withContext(currentCoroutineContext() + InjectedAuthorityContext(authorities)) {
        block()
    }
}

suspend fun withInjectedAuthorities(authorities: List<Authority>, block: suspend () -> Unit) {
    withContext(currentCoroutineContext() + InjectedAuthorityContext(authorities)) {
        block()
    }
}
