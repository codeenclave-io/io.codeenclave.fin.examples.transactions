package io.codeenclave.fin.examples.service.transactions.model.associations

import kotlinx.serialization.Serializable

@Serializable
data class Association(
    val type: AssociationType,
    val value: String,
)

fun generateCounterpartyAssociation(): Association {
    return Association(type = AssociationType.counterparty, counterparty.random())
}

fun generateTradingbookAssociation(): Association {
    return Association(type = AssociationType.tradingbook, tradingbooks.random())
}
