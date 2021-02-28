package io.codeenclave.fin.examples.service.transactions.model

import io.codeenclave.fin.examples.service.transactions.generators.generateTrasnaction
import io.codeenclave.fin.examples.service.transactions.model.associations.Association
import io.codeenclave.fin.examples.service.transactions.model.associations.generateCounterpartyAssociation
import io.codeenclave.fin.examples.service.transactions.model.associations.generateTradingbookAssociation
import io.codeenclave.fin.examples.service.transactions.model.transactions.type.*
import io.codeenclave.fin.examples.service.transactions.model.transactions.type.module
import io.codeenclave.fin.examples.service.transactions.serialization.AssociationListSerializer
import io.codeenclave.fin.examples.service.transactions.serialization.TransactionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import okhttp3.internal.immutableListOf

@Serializable
data class AssociationsTransaction(
    val associations: List<Association>,
    val transaction: Transaction?
)

fun generateAssociationTransaction() : AssociationsTransaction {
    return AssociationsTransaction(
        associations = immutableListOf(
            generateCounterpartyAssociation(),
            generateTradingbookAssociation()
        ),
        transaction = generateTrasnaction()
    )
}

val module = SerializersModule {
    polymorphic(AssociationsTransaction::class)
}

val transactionAssociationsJsonFormatter = Json { serializersModule = module }