package com.karuhun.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class VersionSerializer @Inject constructor() : Serializer<Version> {
    override val defaultValue: Version
        get() = Version.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Version {
        return try {
            Version.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: Version,
        output: OutputStream,
    ) {
        t.writeTo(output)
    }
}