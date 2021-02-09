package io.codeenclave.fin.examples.service.transactions.model.transactions.type

import io.codeenclave.fin.examples.service.transactions.model.transactions.Currency
import io.codeenclave.fin.examples.service.transactions.model.transactions.TransactionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CashBalance(
    var ccy1: Currency,
    var qty: Double
) : Transaction(transactionType = TransactionType.CashBalance)
