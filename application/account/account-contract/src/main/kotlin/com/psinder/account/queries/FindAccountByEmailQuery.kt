package com.psinder.account.queries

import arrow.core.Option
import com.psinder.account.AccountDto
import com.psinder.shared.EmailAddress
import com.trendyol.kediatr.Query

data class FindAccountByEmailQuery(val email: EmailAddress) : Query<FindAccountByEmailQueryResult>

data class FindAccountByEmailQueryResult(val account: Option<AccountDto>)
