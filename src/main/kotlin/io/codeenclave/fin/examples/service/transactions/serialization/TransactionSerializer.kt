package io.codeenclave.fin.examples.service.transactions.serialization

import io.codeenclave.fin.examples.service.transactions.model.transactions.type.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

object TransactionSerializer : KSerializer<Transaction> {
    override val descriptor = PrimitiveSerialDescriptor("Transaction", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Transaction {
        return transactionJsonFormatter.decodeFromString(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Transaction) {
        encoder.encodeString(transactionJsonFormatter.encodeToString(value))
    }

}

