package io.codeenclave.fin.examples.service.transactions.handlers

import io.codeenclave.fin.examples.service.transactions.generators.generateTrasnaction
import io.codeenclave.fin.examples.service.transactions.model.transactions.type.transactionJsonFormatter
import kotlinx.serialization.encodeToString
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK

val generateTransactionHandler: HttpHandler = { req: Request ->
    Response(OK).body(transactionJsonFormatter.encodeToString(generateTrasnaction()))
}