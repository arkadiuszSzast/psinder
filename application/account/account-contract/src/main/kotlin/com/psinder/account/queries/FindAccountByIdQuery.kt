package com.psinder.account.queries

import arrow.core.Option
import com.psinder.account.AccountDto
import com.trendyol.kediatr.Query
import org.litote.kmongo.Id

data class FindAccountByIdQuery(val accountId: Id<AccountDto>) : Query<FindAccountByIdQueryResult>

data class FindAccountByIdQueryResult(val account: Option<AccountDto>)
