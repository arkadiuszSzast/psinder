package com.psinder.auth

import com.psinder.auth.authority.Authority
import com.psinder.auth.authority.FeatureAccessAuthority
import com.psinder.auth.authority.createAccountFeature

internal val unauthorizedUserAuthorities = listOf<Authority>(FeatureAccessAuthority(createAccountFeature))
