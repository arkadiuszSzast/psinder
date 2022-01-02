package com.psinder.account.queries.handlers

import an.awesome.pipelinr.Command
import arrow.core.toOption
import com.psinder.account.AccountDto
import com.psinder.account.AccountRepository
import com.psinder.account.queries.FindAccountByEmailQuery
import com.psinder.account.queries.FindAccountByEmailQueryResult
import kotlinx.coroutines.runBlocking

internal class FindAccountByEmailQueryHandler(private val accountRepository: AccountRepository) :
    Command.Handler<FindAccountByEmailQuery, FindAccountByEmailQueryResult> {
    override fun handle(command: FindAccountByEmailQuery) = runBlocking {
        val email = command.email
        val optAccount = accountRepository.findOneByEmail(email).toOption()

        FindAccountByEmailQueryResult(optAccount.map { AccountDto.fromAccount(it) })
    }
}
