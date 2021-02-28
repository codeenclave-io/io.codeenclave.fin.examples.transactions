package io.codeenclave.fin.examples.service.transactions.handlers

import io.codeenclave.fin.examples.service.transactions.model.generateAssociationTransaction
import io.codeenclave.fin.examples.service.transactions.model.transactionAssociationsJsonFormatter
import kotlinx.serialization.encodeToString
import org.http4k.core.*
import org.http4k.core.Status.Companion.OK

val generateTransactionAssociationHandler: HttpHandler = { req: Request ->
    Response(OK).body(transactionAssociationsJsonFormatter.encodeToString(generateAssociationTransaction()))
}