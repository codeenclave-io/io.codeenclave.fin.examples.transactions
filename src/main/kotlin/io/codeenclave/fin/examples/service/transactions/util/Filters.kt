package io.codeenclave.fin.examples.service.transactions.util

import io.codeenclave.fin.examples.service.transactions.util.ConduitJackson.auto
import org.http4k.core.*
import org.slf4j.LoggerFactory

open class HttpException(val status: Status, message: String = status.description) : RuntimeException(message)
data class GenericErrorModelBody(val body: List<String>)
data class GenericErrorModel(val errors: GenericErrorModelBody)

object CatchHttpExceptions {
    private val logger = LoggerFactory.getLogger(CatchHttpExceptions::class.java)

    operator fun invoke() = Filter { next ->
        {
            try {
                next(it)
            } catch (e: HttpException) {
                logger.error("Uncaught error: ", e)
                createErrorResponse(e.status, listOf(e.message ?: "Oops!"))
            } catch (e: Throwable) {
                logger.error("Uncaught error: ", e)
                createErrorResponse(Status(422, "Unprocessable Entity"), listOf("Unexpected error"))
            }
        }
    }
}

private val error = Body.auto<GenericErrorModel>().toLens()

fun createErrorResponse(status: Status, errorMessages: List<String>) =
    Response(status).with(error of GenericErrorModel(GenericErrorModelBody(errorMessages)))

