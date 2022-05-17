package com.psinder.dog

import com.psinder.shared.validation.Validatable
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class DogName private constructor(val value: String) : Validatable<DogName> {

    companion object {
        operator fun invoke(value: String): DogName {
            return DogName(value.trim().lowercase().replaceFirstChar { it.uppercase() })
        }

        val validator = Validation<DogName> {
            DogName::value {
                minLength(3) hint "validation.dog_name_to_short"
            }
        }
    }

    override val validator: Validation<DogName>
        get() = DogName.validator
}
