package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = PersistentList::class)
class PersistentListSerializer(
    private val dataSerializer: KSerializer<String>
) :
    KSerializer<PersistentList<String>> {
    class PersistentListDescriptor : SerialDescriptor by serialDescriptor<List<String>>() {
        @ExperimentalSerializationApi override val serialName: String =
            "kotlinx.serialization.immutable.persistentList"
    }

    override val descriptor = PersistentListDescriptor()
    override fun serialize(encoder: Encoder, value: PersistentList<String>) {
        return ListSerializer(dataSerializer).serialize(encoder, value.toList())
    }

    override fun deserialize(decoder: Decoder): PersistentList<String> {
        return ListSerializer(dataSerializer).deserialize(decoder).toPersistentList()
    }
}
