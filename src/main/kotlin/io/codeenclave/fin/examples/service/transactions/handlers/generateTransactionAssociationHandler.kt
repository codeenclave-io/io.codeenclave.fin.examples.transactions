package io.codeenclave.fin.examples.service.transactions.handlers

import io.codeenclave.fin.examples.service.transactions.model.generateAssociationTransaction
import io.codeenclave.fin.examples.service.transactions.model.transactionAssociationJsonFormatter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.string

val generateTransactionAssociationHandler: HttpHandler = { req: Request ->
    Response(OK).body(transactionAssociationJsonFormatter.encodeToString(generateAssociationTransaction()))
}