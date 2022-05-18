package com.psinder.dog

internal fun DogProfileDto.Companion.fromDogProfile(dog: DogProfileProjection) =
    DogProfileDto(dog.id.cast(), dog.accountId, dog.votes)
