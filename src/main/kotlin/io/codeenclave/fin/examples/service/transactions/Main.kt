package io.codeenclave.fin.examples.service.transactions

import io.codeenclave.fin.examples.service.transactions.config.local

fun main() {
    val server = startApp(local)
    server.block()
}
