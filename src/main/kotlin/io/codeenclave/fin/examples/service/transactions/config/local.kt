package io.codeenclave.fin.examples.service.transactions.config

import org.http4k.core.Method
import org.http4k.filter.CorsPolicy

val local = AppConfig(
    logConfig = "log4j2-local.yaml",
    corsPolicy = CorsPolicy(
        origins = listOf("localhost:8080"),
        headers = listOf("content-type", "authorization"),
        methods = Method.values().toList(),
        credentials = true
    ),
    port = 8080
)