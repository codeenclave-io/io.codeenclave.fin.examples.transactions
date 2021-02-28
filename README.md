## Getting Started
This project consists of a set of examples and tests for the bi-temporal transaction/association
store io.codeenclave.fin.service.transactions.  This service is available as a horizontally scalable
service built using the <a href=https://opencrux.com/main/index.html>Crux</a> unbundled store.

The store setup uses Kafka in two ways, firstly as the transaction log for Crux, secondly for streaming
query results back to the client.

## Installation
Firstly use helm to install kafka to your local kubernetes cluster.  The best way is to use the helm charts
provided here <a href=https://github.com/confluentinc/cp-helm-charts#manual-test>Confluent Kafka Helm</a>

Add the helm charts to your helm repository

<code>
helm repo add confluentinc https://confluentinc.github.io/cp-helm-charts/
</code>

Then deploy kafka

<code>
helm install my-confluent confluentinc/cp-helm-charts --version 0.6.0 -f k8s/kafka-helm-values.yml
</code>

The <code>kafka-helm-values.yml</code> file contains configuration to expose the kafka cluster externally
to the kubernetes cluster so that you can verify that it is working propoerly before you install the store.
Check that the various pods are up and running correctly.  Additionally the cluster is currently cofigured to have
just 1 kafka broker and 1 zookeeper broker.  This is to limit the resources needed in your local kubernetes cluster.


<pre>
kubectl get pods

NAME                                               READY   STATUS    RESTARTS   AGE
my-confluent-cp-control-center-85779cf5bb-qcvd7    1/1     Running   0          20s
my-confluent-cp-kafka-0                            2/2     Running   0          20s
my-confluent-cp-kafka-1                            2/2     Running   0          13s
my-confluent-cp-kafka-2                            2/2     Running   0          7s
my-confluent-cp-kafka-connect-58d986ff6f-ptx7w     2/2     Running   0          20s
my-confluent-cp-kafka-rest-59bd478985-tkrbl        2/2     Running   0          20s
my-confluent-cp-ksql-server-74f98468cc-h8hd9       2/2     Running   0          20s
my-confluent-cp-schema-registry-85f4b947f8-njtgn   2/2     Running   0          20s
my-confluent-cp-zookeeper-0                        2/2     Running   0          20s
my-confluent-cp-zookeeper-1                        2/2     Running   0          14s
my-confluent-cp-zookeeper-2                        2/2     Running   0          7s
</pre>

Next install the store

<code> kubectl create -f k8s/transaction-service-deployment.yml </code>

Check that all the pods are running

<code>
kubectl get pods
</code>

you should get something similar to this:

<pre>
NAME                                          READY   STATUS    RESTARTS   AGE
association-transaction-node-765886c7-c48cd   1/1     Running   0          19m
association-transaction-node-765886c7-jvvt4   1/1     Running   0          19m
association-transaction-node-765886c7-ppqx4   1/1     Running   0          19m
</pre>

Once everything us up and running it should be possible to see the swagger api by visiting
<a href=http://localhost:32000/index.html#/>http://localhost:32000/index.html#/</a>

## Running 

### [Docker](https://www.docker.com/) container support

1. Build an uberjar of your service: `gradle clean jar`
2. Build a Docker image: `sudo docker build -t codeenclave/io.codeenclave.fin.examples.service.transactions .`
4. Run your Docker image: `docker run -p 8080:8080 codeenclave/io.codeenclave.fin.examples.service.transactions`


## Minio S3 storage
The Transaction/Association can be configured to use a synchronization node for Juxt indicies.  The local 
rocksdb based indices are periodically copied to S3 storage.  This ensures that any new nodes added
(via auto-scaling) will get a pre-built index.  This ensures that new nodes can become available more
quickly, but more importantly that the index can store old records instead of relying on the 
kafka transaction-log to store all transactions.

For trying our S3 storage you can use Minio deployed to your local kubernetes.  The easiest way to do
that is to use the helm charts available at <a href=https://github.com/minio/charts>Minio Kubernetes Helm Charts</a>.

To access Minio from localhost, run the below commands:

1. export POD_NAME=$(kubectl get pods --namespace minio -l "release=minio-1614327680" -o jsonpath="{.items[0].metadata.name}")

2. kubectl port-forward $POD_NAME 9000 --namespace minio

Read more about port forwarding here: http://kubernetes.io/docs/user-guide/kubectl/kubectl_port-forward/

You can now access Minio server on http://localhost:9000. Follow the below steps to connect to Minio server with mc client:

1. Download the Minio mc client - https://docs.minio.io/docs/minio-client-quickstart-guide

2. Get the ACCESS_KEY=$(kubectl get secret minio-1614327680 -o jsonpath="{.data.accesskey}" | base64 --decode) and the SECRET_KEY=$(kubectl get secret minio-1614327680 -o jsonpath="{.data.secretkey}" | base64 --decode)

3. mc alias set minio-1614327680-local http://localhost:9000 "$ACCESS_KEY" "$SECRET_KEY" --api s3v4

4. mc ls minio-1614327680-local

Alternately, you can use your browser or the Minio SDK to access the server - https://docs.minio.io/categories/17
