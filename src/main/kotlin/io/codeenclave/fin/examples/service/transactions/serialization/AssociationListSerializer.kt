package io.codeenclave.fin.examples.service.transactions.serialization

import io.codeenclave.fin.examples.service.transactions.model.associations.Association
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

object AssociationListSerializer : KSerializer<List<Association>> {
    override val descriptor = PrimitiveSerialDescriptor("List<Association>", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): List<Association> {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: List<Association>) {
        encoder.encodeString(value.map { association ->  Json.encodeToString(association)}.toString())
    }

}