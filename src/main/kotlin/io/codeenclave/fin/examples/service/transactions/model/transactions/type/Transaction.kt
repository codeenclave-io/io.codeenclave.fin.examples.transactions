package io.codeenclave.fin.examples.service.transactions.model.transactions.type

import io.codeenclave.fin.examples.service.transactions.model.transactions.TransactionType
import java.util.*

abstract class Transaction (
    var type : TransactionType,
    var tcn : UUID = UUID.randomUUID()
)
