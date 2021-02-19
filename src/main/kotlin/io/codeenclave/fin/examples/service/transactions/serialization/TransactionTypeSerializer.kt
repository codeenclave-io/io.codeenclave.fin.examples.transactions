package io.codeenclave.fin.examples.service.transactions.serialization

import io.codeenclave.fin.examples.service.transactions.model.transactions.TransactionType
import io.codeenclave.fin.examples.service.transactions.model.transactions.type.transactionJsonFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TransactionTypeSerializer : KSerializer<TransactionType> {
    override val descriptor = PrimitiveSerialDescriptor("TransactionType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): TransactionType {
        return TransactionType.valueOf(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: TransactionType) {
        encoder.encodeString(value.type)
    }

}