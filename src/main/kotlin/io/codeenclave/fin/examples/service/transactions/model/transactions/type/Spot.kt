package io.codeenclave.fin.examples.service.transactions.model.transactions.type

import io.codeenclave.fin.examples.service.transactions.model.transactions.CurrencyPair
import io.codeenclave.fin.examples.service.transactions.model.transactions.TransactionType
import java.time.Instant

data class Spot (
    var underlying: CurrencyPair,
    var rate: Double,
    var qty: Double,
    var settlementDate: Instant
) : Transaction(type = TransactionType.Spot)
