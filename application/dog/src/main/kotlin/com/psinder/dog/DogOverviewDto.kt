package com.psinder.dog

import com.psinder.file.storage.PublicFileUrlResolver

internal fun DogOverviewDto.Companion.fromDogOverview(dog: DogOverviewProjection, resolver: PublicFileUrlResolver) =
    DogOverviewDto(dog.id.cast(), dog.name, dog.description, dog.images.map { resolver.resolve(it).toString() })
