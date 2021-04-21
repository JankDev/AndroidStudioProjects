package pl.agh.coronatracker.util

import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.ZonedDateTime

@ExperimentalSerializationApi
@Serializer(forClass = InstantSerializer::class)
object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("kotlinx.datetime.Instant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = Instant.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: Instant) {
        TODO("Not yet implemented")
    }

}

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("java.time.LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate =
        ZonedDateTime.parse(decoder.decodeString()).toLocalDate()


    override fun serialize(encoder: Encoder, value: LocalDate) {
        TODO("Not yet implemented")
    }
}