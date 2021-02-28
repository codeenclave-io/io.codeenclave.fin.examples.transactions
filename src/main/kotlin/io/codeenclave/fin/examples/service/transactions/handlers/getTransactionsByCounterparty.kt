package io.codeenclave.fin.examples.service.transactions.handlers

import io.codeenclave.fin.examples.service.transactions.model.*
import io.codeenclave.fin.examples.service.transactions.model.associations.Association
import io.codeenclave.fin.examples.service.transactions.model.associations.AssociationType
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.streamthoughts.kafka.clients.consumer.consumerConfigsOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import java.lang.Thread.sleep
import java.time.Duration


val getTransactionsByAssociation : HttpHandler =  {request: Request ->
    val name: String = request.query("name") ?: "ctpy1"

    val client = HttpClient(CIO)

    val configs = consumerConfigsOf()
        .client { bootstrapServers(arrayOf("localhost:31090","localhost:31091","localhost:31092")) }
        .groupId("demo-consumer-group")
        .keyDeserializer(StringDeserializer::class.java.name)
        .valueDeserializer(StringDeserializer::class.java.name)


    val associationTransaction = AssociationTransactionQuery(
        Association(AssociationType.counterparty, name),
        HashMap()
    )

    // Response(Status.OK).body(associationTransactionQueryJsonFormatter.encodeToString(associationTransaction))

    var topic = ""
    val job = runBlocking<Unit> {
        val deferred = GlobalScope.async {
            val postResponse = client.get<HttpResponse> {
                contentType(ContentType.Application.Json)
                url("http://localhost:32000/association-transaction/kafka")
                body = associationTransactionQueryJsonFormatter.encodeToString(associationTransaction)
            }

            val kafkaTopic = Json.decodeFromString(KafkaTopic.serializer(), postResponse.readText())
            topic = kafkaTopic.kafkaTopicName

            println("Post response ${postResponse.readText()}")
            println("Parsed response $kafkaTopic")
            println("Connecting to topic $topic")

            sleep(10000)
            val consumer = KafkaConsumer<String, String>(configs)
            consumer.subscribe(listOf(topic))

            while (true) {
                val records = consumer.poll(Duration.ofSeconds(1))
                println("Consumed ${records.count()} records")

                records.iterator().forEach {
                    val message = it.value()
                    println("Message: $message")
                }
            }
        }
        deferred.join()
        client.close()
    }
    Response(Status.OK).body("Publishing to Kafka topic: $topic")
}