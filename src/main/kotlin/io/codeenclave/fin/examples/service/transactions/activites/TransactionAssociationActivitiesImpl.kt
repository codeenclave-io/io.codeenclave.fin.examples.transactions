package io.codeenclave.fin.examples.service.transactions.activites

import io.codeenclave.fin.examples.service.transactions.handlers.RandomTransactionAssociation
import io.codeenclave.fin.examples.service.transactions.model.generateAssociationTransaction
import io.codeenclave.fin.examples.service.transactions.model.transactionAssociationsJsonFormatter
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import io.temporal.activity.Activity

import io.temporal.activity.ActivityExecutionContext
import io.temporal.client.ActivityCompletionClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.lang.Exception
import kotlin.concurrent.thread


class TransactionAssociationActivitiesImpl(
        val completionClient: ActivityCompletionClient): TransactionAssociationActivities {

    override fun generateTransactionAssociation(): String? {
        val ctx = Activity.getExecutionContext()
        val taskToken = ctx.info.taskToken
        thread {
            val client = HttpClient(CIO)
            try {
                val deferred = GlobalScope.async {
                    val association = client.get<String>("http://localhost:8080/association")
                    completionClient.complete(taskToken, association)
                }
            }
            catch (cause: Throwable) {
                completionClient.completeExceptionally(taskToken, cause as Exception?)
            }
            finally {
                client.close()
            }
        }
        ctx.doNotCompleteOnReturn();

        return "this result will be ignored due the asynchronous nature of the activity"
    }
}