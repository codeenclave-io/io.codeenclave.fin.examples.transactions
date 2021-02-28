package io.codeenclave.fin.examples.service.transactions.handlers

import io.codeenclave.fin.examples.service.transactions.model.generateAssociationTransaction
import io.codeenclave.fin.examples.service.transactions.model.transactionAssociationsJsonFormatter
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import okhttp3.internal.wait
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.thread

object RandomTransactionAssociation {
    val threads = HashMap<UUID, Thread>()
    val counters = HashMap<UUID, IntArray>()

    val generateRandomTransactionAssociations: HttpHandler = { req: Request ->
        val parallelExecutions : Int = 20
        val count: Int = req.query("count")?.toInt() ?: 500000
        val rem: Int = count%parallelExecutions
        val fullIterationExecution = count/parallelExecutions
        val jobId = UUID.randomUUID();

        counters.put(jobId, IntArray(parallelExecutions))
        threads.put(jobId, thread {
            for (i in 0..parallelExecutions) {
                val deferred = GlobalScope.async {
                    var iterations = if (i == parallelExecutions) rem else fullIterationExecution
                    val job: Job = launch {
                        println(">>> $i started executing $iterations")
                        for (j in 1..iterations) {
                            val client = HttpClient(CIO)
                            try {
                                val postResponse = client.put<HttpResponse> {
                                    contentType(ContentType.Application.Json)
                                    url("http://localhost:32000/association-transaction")
                                    body = transactionAssociationsJsonFormatter.encodeToString(generateAssociationTransaction())
                                }
                                // println("Received response ($i,$j): ${postResponse.readText()}")
                                counters[jobId]?.set(i, j)
                            }
                            catch (cause: Throwable) {
                                println("Http Post Error ($i, $j): $cause")
                            }
                            // if (j % 20 == 0) counters[jobId]?.set(i, j)
                            client.close()
                        }
                    }
                }
            }
        })
        Response(Status.OK).body(jobId.toString())
    }

    val getStatus: HttpHandler = {request: Request ->
        val jobId: UUID? = UUID.fromString(request.query("jobId"))
        if (threads.containsKey(jobId))
            Response(Status.OK).body(counters[jobId].contentToString())
        else
            Response(Status.OK).body("No such jobId $jobId")
    }
}



