## Getting Started
This project consists of a set of examples and tests for the bi-temporal transaction/association
store io.codeenclave.fin.service.transactions.  This service is available as a horizontally scalable
service built using the <a href=https://opencrux.com/main/index.html>Crux</a> unbundled store.

The store requires both Kafka and Minio and it is assumed that these will be deployed into Kubernetes.
Depending on how these components are deployed the service deployment file <code>transaction-association-deployment.yml</code>
may need to be modified, but see below.


## Installing Kafka
Firstly use helm to install kafka to your local kubernetes cluster.  The best way is to use the helm charts
provided here <a href=https://github.com/confluentinc/cp-helm-charts#manual-test>Confluent Kafka Helm</a>

Add the helm charts to your helm repository

<code>
helm repo add confluentinc https://confluentinc.github.io/cp-helm-charts/
</code>

Create the namespace

<code>
kubectl create ns kafka
</code>

Then deploy kafka

<code>
helm install my-confluent confluentinc/cp-helm-charts --namespace kafka --version 0.6.0 -f k8s/kafka-helm-values.yml
</code>

The <code>kafka-helm-values.yml</code> file contains configuration to expose the kafka cluster externally
to the kubernetes cluster so that you can verify that it is working propoerly before you install the store.
Check that the various pods are up and running correctly.  Additionally the cluster is currently cofigured to have
just 1 kafka broker and 1 zookeeper broker.  This is to limit the resources needed in your local kubernetes cluster.


<pre>
kubectl get pods --namespace kafka

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


## Installing Minio
The Transaction/Association can be configured to use a synchronization node for Juxt indicies.  The local
rocksdb based indices are periodically copied to S3 storage.  This ensures that any new nodes added
(via auto-scaling) will get a pre-built index.  This ensures that new nodes can become available more
quickly, but more importantly that the index can store old records instead of relying on the
kafka transaction-log to store all transactions.

<code>
helm repo add minio https://helm.min.io/
</code>

Create a minio namespace to deploy into

<code>
kubectl create ns minio
</code>

then deploy minio using helm

<code>
helm install --namespace minio --generate-name minio/minio
</code>

Once installed it is important note the name of the node create, something like this

<code>
minio-1615696290.minio.svc.cluster.local
</code>

Edit the <code>transaction-service-deployment.yml</code> file and change the name of the <code>S3_ENDPOINT</code>

<code>
            - name: S3_ENDPOINT
              value: "http://minio-1615696290.minio.svc.cluster.local:9000"
</code>

Next we need to get the access and secret keys for minio, enable access to minio from localhost so that we can
create two s3 buckets for the transaction server.

<pre>
ACCESS_KEY=$(kubectl get secret minio-1615696290 -o jsonpath="{.data.accesskey}" --namespace minio| base64 --decode)
SECRET_KEY=$(kubectl get secret minio-1615696290 -o jsonpath="{.data.secretkey}" --namespace minio| base64 --decode)
</pre>

then run 
<pre>
echo -n $ACCESS_KEY | base64
V2RRMGFtcUNrUVlZNlliQlFGNnU=

echo -n $SECRET_KEY | base64
dFp0Q1RFM0tQY0VnWmNMWXk2TlhGdFVPVlRZdUdVSUhmWWdkWW5Nbg==
</pre>

the two keys need to be copied into the <code>k8s/minio-access-secrets.yml</code> file and then run
<pre>
kubectl create -f k8s/minio-access-secrets.yml
</pre>

now set up access to minio to be accessible from localhost
<pre>
export POD_NAME=$(kubectl get pods --namespace minio -l "release=minio-1615696290" -o jsonpath="{.items[0].metadata.name}")

kubectl port-forward $POD_NAME 9000 --namespace minio
</pre>

in a browser got to <a href=http://localhost:9000>http://localhost:9000 </a>.  Login using the un-encoded access and secret
and keys.  Create two s3 buckets one called associationsync and the other called transactionsync.

## Installing The Store
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
</pre>

Once everything us up and running it should be possible to see the swagger api by visiting
<a href=http://localhost:32000/index.html#/>http://localhost:32000/index.html#/</a>

## Running 

### [Docker](https://www.docker.com/) container support

1. Build an uberjar of your service: `gradle clean jar`
2. Build a Docker image: `sudo docker build -t codeenclave/io.codeenclave.fin.examples.service.transactions .`
4. Run your Docker image: `docker run -p 8080:8080 codeenclave/io.codeenclave.fin.examples.service.transactions`


## Note on Kubernetes AutoScaling
The store deployment uses horizontal scaling, but this only works if the kubernetes cluster has the metrics
server running.  If you are using a kubernetes cluster from Docker Desktop the metrics server is not installed
by default.

You will therefore need to install it.  To do that follow the instructions over here
<a href=https://blog.codewithdan.com/enabling-metrics-server-for-kubernetes-on-docker-desktop/>https://blog.codewithdan.com/enabling-metrics-server-for-kubernetes-on-docker-desktop/ </a>