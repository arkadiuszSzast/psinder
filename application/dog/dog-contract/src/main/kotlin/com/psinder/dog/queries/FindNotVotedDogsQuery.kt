package com.psinder.dog.queries

import com.psinder.dog.DogContext
import com.psinder.dog.DogOverviewDto
import com.trendyol.kediatr.Query
import io.ktor.http.ContentRange
import kotlinx.serialization.Serializable

data class FindNotVotedDogsQuery(val dogContext: DogContext, val range: ContentRange.Bounded) :
    Query<FindNotVotedDogsQueryResponse>

@Serializable
data class FindNotVotedDogsQueryResponse(val dogs: List<DogOverviewDto>)
