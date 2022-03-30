package com.psinder.events

import com.psinder.shared.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class EventMetadata(
    @SerialName("\$correlationId") val correlationId: CorrelationId?,
    @SerialName("\$causationId") val causationId: CausationId?
)

@JvmInline
@Serializable
value class CorrelationId(@Serializable(with = UUIDSerializer::class) val correlationId: UUID)

@JvmInline
@Serializable
value class CausationId(@Serializable(with = UUIDSerializer::class) val causationId: UUID)
