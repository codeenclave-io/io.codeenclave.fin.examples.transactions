package io.codeenclave.fin.examples.service.transactions

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
                    "/ping" bind Method.GET to { _: Request -> Response(Status.OK).body("Ping") }
                )
            )
}