package io.codeenclave.fin.examples.service.transactions.activites

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface TransactionAssociationActivities {
    fun generateTransactionAssociation(): String?
}