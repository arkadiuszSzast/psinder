package com.psinder.auth

data class Authority(val entityRef: Class<*>, val levels: List<AuthorityLevel>)
