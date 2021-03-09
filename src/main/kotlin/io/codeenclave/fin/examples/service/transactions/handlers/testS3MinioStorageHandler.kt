package io.codeenclave.fin.examples.service.transactions.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URI
import java.util.*


object testS3MinioStorageHandler {
    val ACCESS_KEY = "67kurmW6n3bdG9eG3e9E"
    val SECRET_KEY = "zlci9cU2stzdQhnm30AU8Vd5RlgTBINMNWcOm7Bm"
    val credentials: AwsBasicCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)
    val bucketName = "associationstore"
    val region = Region.US_WEST_1

    val storeObjectToS3: HttpHandler = { req: Request ->
        val objectKey = UUID.randomUUID().toString()

        val s3Client = S3Client.builder()
                .endpointOverride(URI.create("http://localhost:9000"))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(region).build()

        val objectRequest : PutObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build()

        s3Client.putObject(objectRequest, RequestBody.fromString(req.bodyString()))

        Response(Status.OK).body(objectKey)
    }

    val getObjectFromS3: HttpHandler = { req: Request ->
        val objectKey = req.query("objectid")

        val s3Client = S3Client.builder()
                .endpointOverride(URI.create("http://localhost:9000"))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(region).build()

        S3AsyncClient.builder()
                .endpointOverride(URI.create("http://localhost:9000"))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(region).build()

        val getObjectRequest : GetObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build()

        val objectResponse = s3Client.getObject(getObjectRequest)

        Response(Status.OK).body(objectResponse.readAllBytes().decodeToString())
    }
}