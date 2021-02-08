package io.codeenclave.fin.examples.service.transactions.model.transactions

enum class TransactionType(val type: String) {
    CashBalance("CashBalance"),
    Spot("Spot"),
    Swap("Swap")
}