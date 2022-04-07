package com.psinder.account.queries

import arrow.core.toOption
import com.psinder.account.AccountDto
import com.psinder.account.AccountRepository
import com.psinder.account.fromAccount
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.trendyol.kediatr.AsyncQueryHandler
import mu.KotlinLogging

internal class FindAccountByEmailQueryHandler(
    private val accountRepository: AccountRepository,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncQueryHandler<FindAccountByEmailQuery, FindAccountByEmailQueryResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(query: FindAccountByEmailQuery): FindAccountByEmailQueryResult {
        val email = query.email

        return accountRepository.findOneByEmail(email).toOption()
            .tap { logger.debug { "Found account with email: [$email]" } }
            .tapNone { logger.debug { "Account with email: [$email] not found" } }
            .filter { acl.canView(it).toBoolean() }
            .map { AccountDto.fromAccount(it) }
            .let { FindAccountByEmailQueryResult(it) }
    }
}
