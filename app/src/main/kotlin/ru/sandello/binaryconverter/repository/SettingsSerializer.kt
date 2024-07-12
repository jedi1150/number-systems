package ru.sandello.binaryconverter.repository

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import ru.sandello.binaryconverter.Settings
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class SettingsSerializer @Inject constructor() : Serializer<Settings> {

    override val defaultValue: Settings = Settings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Settings {
        try {
            return Settings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) = t.writeTo(output)
}
