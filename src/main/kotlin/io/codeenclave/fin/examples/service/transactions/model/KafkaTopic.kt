package io.codeenclave.fin.examples.service.transactions.model

import kotlinx.serialization.Serializable

@Serializable
data class KafkaTopic(
    val kafkaTopicName: String,
    val nodeName: String
)
