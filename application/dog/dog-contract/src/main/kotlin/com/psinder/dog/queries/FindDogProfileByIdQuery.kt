package com.psinder.dog.queries

import arrow.core.Option
import com.psinder.dog.DogProfileDto
import com.trendyol.kediatr.Query
import org.litote.kmongo.Id

data class FindDogProfileByIdQuery(val dogId: Id<DogProfileDto>) : Query<FindDogProfileByIdQueryResult>

data class FindDogProfileByIdQueryResult(val dog: Option<DogProfileDto>)
