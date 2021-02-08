package io.codeenclave.fin.examples.service.transactions.generators

import io.codeenclave.fin.examples.service.transactions.model.transactions.TransactionType
import io.codeenclave.fin.examples.service.transactions.model.transactions.getCurrency
import io.codeenclave.fin.examples.service.transactions.model.transactions.getCurrencyPair
import io.codeenclave.fin.examples.service.transactions.model.transactions.type.CashBalance
import io.codeenclave.fin.examples.service.transactions.model.transactions.type.Spot
import io.codeenclave.fin.examples.service.transactions.model.transactions.type.Swap
import io.codeenclave.fin.examples.service.transactions.model.transactions.type.Transaction
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.random.Random


fun generateCashBalance() : CashBalance {
    return CashBalance(getCurrency(), Random.nextDouble(0.0, Double.MAX_VALUE))
}

fun generateSpot() : Spot {
    return Spot(getCurrencyPair(), Random.nextDouble(), Random.nextDouble(0.0, Double.MAX_VALUE), Instant.now().plus(2, ChronoUnit.DAYS))
}

fun generateSwap(): Swap {
    return Swap(getCurrencyPair(), Random.nextDouble(), Random.nextDouble(0.0, Double.MAX_VALUE),
        Instant.now().plus(2, ChronoUnit.DAYS),
        Instant.now().plus(Random.nextLong(2, 1000), ChronoUnit.DAYS))
}

private val transactionGenerators : HashMap<TransactionType, () -> Transaction> = hashMapOf(
    TransactionType.CashBalance to {generateCashBalance()},
    TransactionType.Spot to {generateSpot()},
    TransactionType.Swap to {generateSwap()}
)


fun generateTrasnaction() : Transaction? {
    return transactionGenerators.get(TransactionType.values().random())?.let { it() }
}