package com.karuhun.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class HotelProfileSerializer @Inject constructor() : Serializer<Hotel> {
    override val defaultValue: Hotel
        get() = Hotel.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Hotel {
        return try {
            Hotel.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: Hotel,
        output: OutputStream,
    ) {
        t.writeTo(output)
    }
}