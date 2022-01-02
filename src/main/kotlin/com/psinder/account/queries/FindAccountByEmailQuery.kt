package com.psinder.account.queries

import an.awesome.pipelinr.Command
import arrow.core.Option
import com.psinder.account.AccountDto
import com.psinder.shared.EmailAddress

internal data class FindAccountByEmailQuery(val email: EmailAddress) : Command<FindAccountByEmailQueryResult>

internal data class FindAccountByEmailQueryResult(val accountDto: Option<AccountDto>)
