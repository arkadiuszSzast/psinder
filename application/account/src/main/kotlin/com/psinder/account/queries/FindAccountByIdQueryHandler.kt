package com.psinder.account.queries

import com.psinder.account.AccountDto
import com.psinder.account.AccountRepository
import com.psinder.account.fromAccount
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.trendyol.kediatr.AsyncQueryHandler
import mu.KotlinLogging

internal class FindAccountByIdQueryHandler(
    private val accountRepository: AccountRepository,
    private val acl: AuthorizedAccountAbilityProvider
) : AsyncQueryHandler<FindAccountByIdQuery, FindAccountByIdQueryResult> {
    private val logger = KotlinLogging.logger {}

    override suspend fun handleAsync(query: FindAccountByIdQuery): FindAccountByIdQueryResult {
        val id = query.accountId

        return accountRepository.findById(id.cast())
            .tap { logger.debug { "Found account with id: [$id]" } }
            .tapNone { logger.debug { "Account with id: [$id] not found" } }
            .filter { acl.canView(it).toBoolean() }
            .map { AccountDto.fromAccount(it) }
            .let { FindAccountByIdQueryResult(it) }
    }
}
