package com.psinder.account.queries

import arrow.core.toOption
import com.psinder.account.AccountDto
import com.psinder.account.AccountRepository
import com.psinder.account.fromAccount
import com.trendyol.kediatr.AsyncQueryHandler
import mu.KotlinLogging

internal class FindAccountByEmailQueryHandler(private val accountRepository: AccountRepository) :
    AsyncQueryHandler<FindAccountByEmailQuery, FindAccountByEmailQueryResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(query: FindAccountByEmailQuery): FindAccountByEmailQueryResult {
        val email = query.email
        val optAccount = accountRepository.findOneByEmail(email).toOption()
            .tapNone { logger.debug { "Found account with email: [$email]" } }
            .tap { logger.debug { "Account with email: [$email] not found" } }

        return FindAccountByEmailQueryResult(optAccount.map { AccountDto.fromAccount(it) })
    }
}
