package io.codeenclave.fin.examples.service.transactions

import io.codeenclave.fin.examples.service.transactions.handlers.*
import io.codeenclave.fin.examples.service.transactions.handlers.testS3MinioStorageHandler.getObjectFromS3
import io.codeenclave.fin.examples.service.transactions.handlers.testS3MinioStorageHandler.storeObjectToS3
import io.codeenclave.fin.examples.service.transactions.util.CatchHttpExceptions
import io.codeenclave.fin.examples.service.transactions.util.createErrorResponse
import org.http4k.core.*
import org.http4k.filter.CorsPolicy
import org.http4k.filter.ServerFilters
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes


class Router(
    val corsPolicy: CorsPolicy
) {

    operator fun invoke(): RoutingHttpHandler =
        CatchHttpExceptions()
            .then(ServerFilters.Cors(corsPolicy))
            .then(ServerFilters.CatchLensFailure { error ->
                createErrorResponse(
                    Status.BAD_REQUEST,
                    if (error.cause != null) listOf(error.cause?.message!!) else error.failures.map { it.toString() } // TODO: improve error message creation logic
                )
            })
            // .then(ServerFilters.InitialiseRequestContext(contexts))
            .then(
                routes(
                    "/ping" bind Method.GET to { _: Request -> Response(Status.OK).body("Ping")},
                    "/transaction" bind Method.GET to generateTransactionHandler,
                    "/association" bind Method.GET to generateTransactionAssociationHandler,
                    "/generate" bind Method.GET to RandomTransactionAssociation.generateRandomTransactionAssociations,
                        "/generate/status" bind Method.GET to RandomTransactionAssociation.getStatus,
                    "/association/counterparty" bind Method.GET to getTransactionsByAssociation,
                        "/s3" bind Method.PUT to storeObjectToS3,
                "/s3" bind Method.GET to getObjectFromS3
                )
            )
}