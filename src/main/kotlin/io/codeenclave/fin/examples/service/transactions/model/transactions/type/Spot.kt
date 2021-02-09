package io.codeenclave.fin.examples.service.transactions.model.transactions.type

import io.codeenclave.fin.examples.service.transactions.model.transactions.CurrencyPair
import io.codeenclave.fin.examples.service.transactions.model.transactions.TransactionType
import io.codeenclave.fin.examples.service.transactions.serialization.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Spot (
    var underlying: CurrencyPair,
    var rate: Double,
    var qty: Double,
    @Serializable(with = InstantSerializer::class)
    var settlementDate: Instant
) : Transaction(transactionType = TransactionType.Spot)
