package com.psinder.account.queries

import arrow.core.toOption
import com.psinder.account.AccountDto
import com.psinder.account.AccountRepository
import com.psinder.account.fromAccount
import com.trendyol.kediatr.AsyncQueryHandler

internal class FindAccountByEmailQueryHandler(private val accountRepository: AccountRepository) :
    AsyncQueryHandler<FindAccountByEmailQuery, FindAccountByEmailQueryResult> {
    override suspend fun handleAsync(query: FindAccountByEmailQuery): FindAccountByEmailQueryResult {
        val email = query.email
        val optAccount = accountRepository.findOneByEmail(email).toOption()

        return FindAccountByEmailQueryResult(optAccount.map { AccountDto.fromAccount(it) })
    }
}
