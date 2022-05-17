package com.psinder.dog

import com.psinder.dog.requests.RegisterDogRequest
import io.github.serpro69.kfaker.Faker

val Faker.dogModule: DogModule
    get() = DogModule(this)

class DogModule(private val faker: Faker) {
    fun dogName() = DogName(faker.dog.name())
    fun dogDescription() =
        DogDescription("${faker.dog.breed()} age: ${faker.dog.age()} name: ${faker.dog.name()} ${faker.dog.memePhrase()}")

    fun registerDogRequest(images: List<DogProfileImageUrl> = emptyList()) =
        RegisterDogRequest(dogName(), dogDescription(), images)
}
