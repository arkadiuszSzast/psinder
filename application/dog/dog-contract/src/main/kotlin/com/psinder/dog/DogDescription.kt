package com.psinder.dog

import com.psinder.shared.validation.Validatable
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class DogDescription(val value: String) : Validatable<DogDescription> {

    companion object {
        val validator = Validation<DogDescription> {
            DogDescription::value {
                minLength(10) hint "validation.dog_description_to_short"
            }
        }
    }

    override val validator: Validation<DogDescription>
        get() = DogDescription.validator
}