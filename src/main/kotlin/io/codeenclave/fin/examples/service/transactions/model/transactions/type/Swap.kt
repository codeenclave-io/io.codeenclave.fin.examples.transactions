package io.codeenclave.fin.examples.service.transactions.model.transactions.type

import io.codeenclave.fin.examples.service.transactions.model.transactions.CurrencyPair
import io.codeenclave.fin.examples.service.transactions.model.transactions.TransactionType
import io.codeenclave.fin.examples.service.transactions.serialization.InstantSerializer
import io.codeenclave.fin.examples.service.transactions.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

@Serializable
data class Swap(
    var underlying: CurrencyPair,
    var rate: Double,
    var qty: Double,
    @Serializable(with = InstantSerializer::class)
    var nearSettlementDate: Instant,
    @Serializable(with = InstantSerializer::class)
    var farSettlementDate: Instant
) : Transaction(transactionType = TransactionType.Swap)
