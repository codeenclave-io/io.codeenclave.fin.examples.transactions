package io.codeenclave.fin.examples.service.transactions

import io.codeenclave.fin.examples.service.transactions.config.AppConfig
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer
import java.util.logging.Logger


fun startApp(config : AppConfig) : Http4kServer {
    val logger = Logger.getLogger("main")

    val app = Router(config.corsPolicy)()

    logger.info("Starting server...")
    val server = app.asServer(Jetty(config.port)).start()
    logger.info("Server started on port ${config.port}")
    return server
}