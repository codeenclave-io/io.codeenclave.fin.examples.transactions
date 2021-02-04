package io.codeenclave.fin.examples.service.transactions.config

import org.http4k.filter.CorsPolicy

data class AppConfig(
    val logConfig: String,
    val corsPolicy: CorsPolicy,
    val port: Int
)