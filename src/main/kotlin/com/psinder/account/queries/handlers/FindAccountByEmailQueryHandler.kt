package com.psinder.account.queries.handlers

import arrow.core.toOption
import com.psinder.account.AccountDto
import com.psinder.account.AccountRepository
import com.psinder.account.queries.FindAccountByEmailQuery
import com.psinder.account.queries.FindAccountByEmailQueryResult
import com.trendyol.kediatr.AsyncQueryHandler
import kotlinx.coroutines.runBlocking

internal class FindAccountByEmailQueryHandler(private val accountRepository: AccountRepository) :
    AsyncQueryHandler<FindAccountByEmailQuery, FindAccountByEmailQueryResult> {
    override suspend fun handleAsync(query: FindAccountByEmailQuery) = runBlocking {
        val email = query.email
        val optAccount = accountRepository.findOneByEmail(email).toOption()

        FindAccountByEmailQueryResult(optAccount.map { AccountDto.fromAccount(it) })
    }
}
