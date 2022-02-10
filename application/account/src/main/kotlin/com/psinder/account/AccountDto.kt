package com.psinder.account

internal fun AccountDto.Companion.fromAccount(account: Account): AccountDto {
    return AccountDto(
        account.id.cast(),
        account.email,
        account.personalData,
        account.status,
        account.timeZoneId,
        account.lastLoggedInDate
    )
}
