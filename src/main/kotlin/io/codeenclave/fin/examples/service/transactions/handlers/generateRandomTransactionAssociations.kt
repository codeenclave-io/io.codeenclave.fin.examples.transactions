package io.codeenclave.fin.examples.service.transactions.handlers

import io.codeenclave.fin.examples.service.transactions.model.AssociationsTransaction
import io.codeenclave.fin.examples.service.transactions.model.generateAssociationTransaction
import io.codeenclave.fin.examples.service.transactions.model.transactionAssociationJsonFormatter
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status

val generateRandomTransactionAssociations: HttpHandler = { req: Request ->
    val divisor : Int = 10
    val count: Int = req.query("count")?.toInt() ?: 500000
    val rem: Int = count%divisor
    val blockCount = count/divisor + (if (rem > 0)  1 else 0)
    for(i in 1..blockCount) {
        runBlocking {
            var iterations = if (i == blockCount) rem else divisor
            val job = launch {
                println("${Thread.currentThread()} has run. $i")
                val client = HttpClient(CIO)
                for(j in 1..iterations) {
                    val response: HttpResponse = client.get("http://localhost:32000/about")
                    println(response.status)
                    println(response.readText())

                    val at = generateAssociationTransaction()
                    val gat = transactionAssociationJsonFormatter.encodeToString(at)
                    println(gat)
                    val postResponnse = client.put<HttpResponse>{
                        contentType(ContentType.Application.Json)
                        url("http://localhost:32000/association-transaction")
                        body = gat
                    }
                    println(postResponnse.readText())
                }
                client.close()
            }
        }
    }
    Response(Status.OK).body(count.toString())
}

