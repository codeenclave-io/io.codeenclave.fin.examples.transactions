package io.codeenclave.fin.examples.service.transactions.controllers

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun MyServer(port: Int): Http4kServer = { _: Request -> Response(OK) }.asServer(Jetty(port))