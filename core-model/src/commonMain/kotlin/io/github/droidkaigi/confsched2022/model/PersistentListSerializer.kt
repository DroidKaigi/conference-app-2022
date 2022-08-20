package io.github.droidkaigi.confsched2022.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
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

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = PersistentList::class)
class ImmutableListSerializer(
    private val dataSerializer: KSerializer<String>
) :
    KSerializer<ImmutableList<String>> {
    class ImmutableListDescriptor : SerialDescriptor by serialDescriptor<List<String>>() {
        @ExperimentalSerializationApi override val serialName: String =
            "kotlinx.serialization.immutable.persistentList"
    }

    override val descriptor = ImmutableListDescriptor()
    override fun serialize(encoder: Encoder, value: ImmutableList<String>) {
        return ListSerializer(dataSerializer).serialize(encoder, value.toList())
    }

    override fun deserialize(decoder: Decoder): ImmutableList<String> {
        return ListSerializer(dataSerializer).deserialize(decoder).toImmutableList()
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = ImmutableSet::class)
class ImmutableSetSerializer(
    private val dataSerializer: KSerializer<String>
) :
    KSerializer<ImmutableSet<String>> {
    class ImmutableSetDescriptor : SerialDescriptor by serialDescriptor<List<String>>() {
        @ExperimentalSerializationApi override val serialName: String =
            "kotlinx.serialization.immutable.persistentList"
    }

    override val descriptor = ImmutableSetDescriptor()
    override fun serialize(encoder: Encoder, value: ImmutableSet<String>) {
        return ListSerializer(dataSerializer).serialize(encoder, value.toList())
    }

    override fun deserialize(decoder: Decoder): ImmutableSet<String> {
        return ListSerializer(dataSerializer).deserialize(decoder).toImmutableSet()
    }
}
