package com.psinder.dog.requests

import com.psinder.dog.DogDescription
import com.psinder.dog.DogName
import com.psinder.dog.DogProfileImageUrl
import com.psinder.shared.validation.Validatable
import io.konform.validation.Validation
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDogRequest(
    val dogName: DogName,
    val dogDescription: DogDescription,
    val images: List<DogProfileImageUrl>
) : Validatable<RegisterDogRequest> {

    companion object {
        val validator = Validation<RegisterDogRequest> {
            RegisterDogRequest::dogName {
                run(DogName.validator)
            }
            RegisterDogRequest::dogDescription {
                run(DogDescription.validator)
            }
        }
    }

    override val validator: Validation<RegisterDogRequest>
        get() = RegisterDogRequest.validator
}
