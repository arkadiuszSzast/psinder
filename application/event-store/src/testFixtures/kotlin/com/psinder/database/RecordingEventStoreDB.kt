package com.psinder.database

import arrow.core.computations.ResultEffect.bind
import com.eventstore.dbclient.AppendToStreamOptions
import com.eventstore.dbclient.DeleteResult
import com.eventstore.dbclient.DeleteStreamOptions
import com.eventstore.dbclient.Direction
import com.eventstore.dbclient.EventData
import com.eventstore.dbclient.PersistentSubscription
import com.eventstore.dbclient.Position
import com.eventstore.dbclient.ReadAllOptions
import com.eventstore.dbclient.ReadResult
import com.eventstore.dbclient.ReadStreamOptions
import com.eventstore.dbclient.RecordedEvent
import com.eventstore.dbclient.ResolvedEvent
import com.eventstore.dbclient.StreamRevision
import com.eventstore.dbclient.SubscribeToAllOptions
import com.eventstore.dbclient.SubscribeToStreamOptions
import com.eventstore.dbclient.Subscription
import com.eventstore.dbclient.WriteResult
import com.psinder.events.CorrelationId
import com.psinder.events.getMetadata
import io.traxter.eventstoredb.EventStoreDB
import io.traxter.eventstoredb.PersistentSubscriptionOptions
import io.traxter.eventstoredb.Prefix
import io.traxter.eventstoredb.StreamGroup
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class RecordingEventStoreDB : EventStoreDB {
    private var streams: MutableMap<Stream, MutableList<EventData>> = mutableMapOf()

    fun clean() {
        streams = mutableMapOf()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val parent: CompletableJob = Job()
    override val coroutineContext: CoroutineContext
        get() = parent

    override suspend fun appendToStream(
        streamName: String,
        eventData: EventData,
        options: AppendToStreamOptions
    ): WriteResult {
        val key = streams.keys.find { it.name == StreamName(streamName) }
            ?: Stream(UUID.randomUUID().toString(), StreamName(streamName))
        val value = streams.getOrPut(key) { mutableListOf() }
        value.add(eventData)
        return WriteResult(StreamRevision(value.size.toLong()), Position(value.size.toLong(), value.size.toLong()))
    }

    override suspend fun appendToStream(
        streamName: String,
        eventType: String,
        message: String,
        options: AppendToStreamOptions
    ) = appendToStream(streamName, EventData.builderAsJson(eventType, message).build(), options)

    override suspend fun deleteStream(streamName: StreamName): DeleteResult {
        streams.keys.filter { it.name == streamName }
            .forEach { streams.remove(it) }

        return DeleteResult(Position.END)
    }

    override suspend fun deleteStream(streamName: StreamName, options: DeleteStreamOptions.() -> Unit) =
        deleteStream(streamName)

    override suspend fun readAll() = readAll(ReadAllOptions.get())

    override suspend fun readAll(options: ReadAllOptions) = readAll(Int.MAX_VALUE.toLong(), options)

    override suspend fun readAll(maxCount: Long) = readAll(maxCount, ReadAllOptions.get())

    override suspend fun readAll(maxCount: Long, options: ReadAllOptions): ReadResult {
        return streams.flatMap { (key, value) ->
            val sortedValues = if (options.direction == Direction.Forwards) {
                value
            } else {
                value.reversed()
            }
            sortedValues.take(maxCount.toInt()).mapIndexed { index, event ->
                val recordedEvent = RecordedEvent(
                    key.id,
                    StreamRevision(value.size.toLong()),
                    event.eventId,
                    Position(index.toLong(), index.toLong()),
                    event.getSystemMetadata(),
                    event.eventData,
                    event.userMetadata
                )
                ResolvedEvent(recordedEvent, recordedEvent)
            }
        }.let { ReadResult(it) }
    }

    override suspend fun readByCorrelationId(id: UUID): ReadResult {
        return readAll().events
            .filter { it.event.getMetadata().orNone().isDefined() }
            .filter { it.event.getMetadata().bind().correlationId == CorrelationId(id) }
            .let { ReadResult(it) }
    }

    override suspend fun readStream(streamName: StreamName) = readStream(streamName, ReadStreamOptions.get())

    override suspend fun readStream(streamName: StreamName, options: ReadStreamOptions) =
        readStream(streamName, Int.MAX_VALUE.toLong(), options)

    override suspend fun readStream(streamName: StreamName, maxCount: Long) =
        readStream(streamName, maxCount, ReadStreamOptions.get())

    override suspend fun readStream(streamName: StreamName, maxCount: Long, options: ReadStreamOptions): ReadResult {
        return streams
            .filterKeys { it.name == streamName }
            .flatMap { (key, value) ->
                val sortedValues = if (options.direction == Direction.Forwards) {
                    value
                } else {
                    value.reversed()
                }
                sortedValues.take(maxCount.toInt()).mapIndexed { index, event ->
                    val recordedEvent = RecordedEvent(
                        key.id,
                        StreamRevision(value.size.toLong()),
                        event.eventId,
                        Position(index.toLong(), index.toLong()),
                        event.getSystemMetadata(),
                        event.eventData,
                        event.userMetadata
                    )
                    ResolvedEvent(recordedEvent, recordedEvent)
                }
            }.let { ReadResult(it) }
    }

    override suspend fun subscribeByCorrelationId(id: UUID, listener: suspend ResolvedEvent.() -> Unit): Subscription {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeByEventTypeFiltered(
        prefix: Prefix,
        listener: suspend ResolvedEvent.() -> Unit
    ): Subscription {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeByEventTypeFiltered(
        regex: Regex,
        listener: suspend ResolvedEvent.() -> Unit
    ): Subscription {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeByStreamNameFiltered(
        prefix: Prefix,
        listener: suspend ResolvedEvent.() -> Unit
    ): Subscription {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeByStreamNameFiltered(
        regex: Regex,
        listener: suspend ResolvedEvent.() -> Unit
    ): Subscription {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeToAll(
        options: SubscribeToAllOptions,
        listener: suspend ResolvedEvent.() -> Unit
    ): Subscription {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeToAll(listener: suspend ResolvedEvent.() -> Unit): Subscription {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeToPersistedStream(
        streamName: StreamName,
        groupName: StreamGroup,
        listener: suspend (subscription: PersistentSubscription, event: ResolvedEvent) -> Unit
    ): PersistentSubscription {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeToPersistedStream(
        streamName: StreamName,
        groupName: StreamGroup,
        options: PersistentSubscriptionOptions,
        listener: suspend (subscription: PersistentSubscription, event: ResolvedEvent) -> Unit
    ): PersistentSubscription {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeToStream(
        streamName: StreamName,
        options: SubscribeToStreamOptions,
        listener: suspend ResolvedEvent.() -> Unit
    ): Subscription {
        TODO("Not yet implemented")
    }

    override suspend fun subscribeToStream(
        streamName: StreamName,
        listener: suspend ResolvedEvent.() -> Unit
    ): Subscription {
        TODO("Not yet implemented")
    }
}

class Stream(val id: String, val name: StreamName)
