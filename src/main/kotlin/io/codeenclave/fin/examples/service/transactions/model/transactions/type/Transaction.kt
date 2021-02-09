package io.codeenclave.fin.examples.service.transactions.model.transactions.type

import io.codeenclave.fin.examples.service.transactions.model.transactions.TransactionType
import io.codeenclave.fin.examples.service.transactions.serialization.UUIDSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.util.*

@Polymorphic
@Serializable
abstract class Transaction (
    var transactionType : TransactionType,
    @Serializable(with = UUIDSerializer::class)
    var tcn : UUID = UUID.randomUUID()
)


val module = SerializersModule {
    polymorphic(Transaction::class) {
        subclass(CashBalance::class)
        subclass(Spot::class)
        subclass(Swap::class)
    }
}

val transactionJsonFormatter = Json { serializersModule = module }
