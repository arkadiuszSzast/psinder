package com.psinder.account.activation

import arrow.core.Either
import arrow.core.left
import arrow.core.nel
import arrow.core.right
import arrow.optics.optics
import arrow.optics.snoc
import com.psinder.auth.account.AccountId
import com.psinder.auth.account.BelongsToAccount
import com.psinder.database.HasId
import com.psinder.shared.jwt.JwtToken
import com.psinder.shared.validation.ValidationError
import com.psinder.shared.validation.ValidationException
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@optics
@Serializable
data class AccountActivationTokens constructor(
    @SerialName("_id") @Contextual override val id: Id<AccountActivationTokens>,
    override val accountId: AccountId,
    val tokens: List<ActivationToken>
) : HasId<AccountActivationTokens>, BelongsToAccount {

    companion object {
        fun create(accountId: AccountId, tokens: List<ActivationToken>) =
            AccountActivationTokens(newId(), accountId, tokens)

        fun appendToken(target: AccountActivationTokens, token: ActivationToken) =
            tokens.modify(target) { it snoc token }
    }
}

@optics
@Serializable
data class ActivationToken(val token: JwtToken, val used: Boolean = false) {

    companion object {
        fun use(target: ActivationToken): Either<ValidationException, ActivationToken> {
            return when (target.used) {
                true -> ValidationException(
                    ValidationError(
                        ".used",
                        "validation.account_activate_token_already_used"
                    ).nel()
                ).left()
                false -> used.set(target, true).right()
            }
        }
    }
}
