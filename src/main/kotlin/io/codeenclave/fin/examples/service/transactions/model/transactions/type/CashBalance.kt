package io.codeenclave.fin.examples.service.transactions.model.transactions.type

import io.codeenclave.fin.examples.service.transactions.model.transactions.Currency
import io.codeenclave.fin.examples.service.transactions.model.transactions.TransactionType

data class CashBalance(
    var ccy1: Currency,
    var qty: Double
) : Transaction(type = TransactionType.CashBalance)
