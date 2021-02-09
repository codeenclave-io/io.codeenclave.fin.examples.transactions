package io.codeenclave.fin.examples.service.transactions.model

import io.codeenclave.fin.examples.service.transactions.generators.generateTrasnaction
import io.codeenclave.fin.examples.service.transactions.model.associations.Association
import io.codeenclave.fin.examples.service.transactions.model.associations.generateCounterpartyAssociation
import io.codeenclave.fin.examples.service.transactions.model.associations.generateTradingbookAssociation
import io.codeenclave.fin.examples.service.transactions.model.transactions.type.Transaction
import io.codeenclave.fin.examples.service.transactions.serialization.TransactionSerializer
import kotlinx.serialization.Serializable
import okhttp3.internal.immutableListOf

@Serializable
data class AssociationsTransaction(
    val associations: List<Association>,
    @Serializable(with = TransactionSerializer::class)
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