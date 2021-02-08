package io.codeenclave.fin.examples.service.transactions.model.transactions.type

import io.codeenclave.fin.examples.service.transactions.model.transactions.CurrencyPair
import io.codeenclave.fin.examples.service.transactions.model.transactions.TransactionType
import java.time.Instant

data class Swap(
    var underlying: CurrencyPair,
    var rate: Double,
    var qty: Double,
    var nearSettlementDate: Instant,
    var farSettlementDate: Instant
) : Transaction(type = TransactionType.Swap)
