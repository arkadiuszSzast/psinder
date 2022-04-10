package com.psinder.account

internal fun AccountDto.Companion.fromAccount(accountAggregate: AccountProjection): AccountDto {
    return AccountDto(
        accountAggregate.id.cast(),
        accountAggregate.email,
        accountAggregate.personalData,
        accountAggregate.created,
        accountAggregate.status,
        accountAggregate.timeZoneId,
        accountAggregate.lastLoggedInDate
    )
}
