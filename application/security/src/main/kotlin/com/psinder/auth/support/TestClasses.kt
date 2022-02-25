package com.psinder.auth.support

import com.psinder.auth.account.AccountId
import com.psinder.auth.account.BelongsToAccount

data class Dog(override val accountId: AccountId) : BelongsToAccount
data class Cat(override val accountId: AccountId) : BelongsToAccount
data class Turtle(val age: Long)
data class Cow(val name: String)
