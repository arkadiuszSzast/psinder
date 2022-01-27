package com.psinder.shared.password

import com.psinder.shared.validation.Validatable
import io.konform.validation.Validation
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@JvmInline
@Serializable
internal value class RawPassword(val value: String) : Validatable<RawPassword> {

    fun hashpw() = HashedPassword(BCrypt.hashpw(value, BCrypt.gensalt()))

    companion object {
        val validator = Validation<RawPassword> {
            RawPassword::value {
                run(defaultPasswordValidator)
            }
        }
    }

    override val validator: Validation<RawPassword>
        get() = RawPassword.validator
}

@JvmInline
@Serializable
internal value class HashedPassword(val value: String) {
    fun matches(value: String) = BCrypt.checkpw(value, this.value)
}
