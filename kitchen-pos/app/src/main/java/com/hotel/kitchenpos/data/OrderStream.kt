package com.hotel.kitchenpos.data

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import java.util.concurrent.TimeUnit

/** A frame surfaced by [OrderStream.connect]. */
sealed interface StreamEvent {
    data object Connected : StreamEvent
    data object Disconnected : StreamEvent
    data class Order(val order: OrderDTO) : StreamEvent
}

/**
 * Live order feed over Server-Sent Events (`GET /api/orders/stream`),
 * mirroring the web POS's `useOrderStream`. EventSource auto-reconnects;
 * we surface connect/disconnect so the header can show a live indicator.
 */
object OrderStream {
    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    // No read timeout — the stream is intentionally long-lived.
    private val sseClient: OkHttpClient = KitchenApi.client.newBuilder()
        .readTimeout(0, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    fun connect(): Flow<StreamEvent> = callbackFlow {
        val request = Request.Builder()
            .url("${AppSession.baseUrl}/api/orders/stream")
            .header("Accept", "text/event-stream")
            .build()

        val listener = object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                trySend(StreamEvent.Connected)
            }

            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String,
            ) {
                val event = runCatching {
                    json.decodeFromString(OrderEvent.serializer(), data)
                }.getOrNull() ?: return
                if (event.type == "ready") {
                    trySend(StreamEvent.Connected)
                } else {
                    event.order?.let { trySend(StreamEvent.Order(it)) }
                }
            }

            override fun onClosed(eventSource: EventSource) {
                trySend(StreamEvent.Disconnected)
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?,
            ) {
                trySend(StreamEvent.Disconnected)
            }
        }

        val source = EventSources.createFactory(sseClient).newEventSource(request, listener)
        awaitClose { source.cancel() }
    }
}
