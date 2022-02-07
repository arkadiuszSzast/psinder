package com.psinder.account.queries

import arrow.core.Option
import com.psinder.account.AccountDto
import com.psinder.shared.EmailAddress
import com.trendyol.kediatr.Query

internal data class FindAccountByEmailQuery(val email: EmailAddress) : Query<FindAccountByEmailQueryResult>

internal data class FindAccountByEmailQueryResult(val accountDto: Option<com.psinder.account.AccountDto>)
