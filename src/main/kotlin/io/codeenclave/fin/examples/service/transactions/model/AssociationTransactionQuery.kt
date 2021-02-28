package io.codeenclave.fin.examples.service.transactions.model

import io.codeenclave.fin.examples.service.transactions.model.associations.Association
import io.codeenclave.fin.examples.service.transactions.model.transactions.type.Transaction
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@Serializable
data class AssociationTransactionQuery(
    val association: Association,
    val transaction: Map<String,String>
)

val AssociationTransactionQueryModule = SerializersModule {
    polymorphic(AssociationTransactionQuery::class)
}

val associationTransactionQueryJsonFormatter = Json {
    serializersModule = AssociationTransactionQueryModule
}