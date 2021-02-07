## Getting Started
This project consists of a set of examples and tests for the bi-temporal transaction/association
store io.codeenclave.fin.service.transactions.  This service is available as a horizontally scalable
service built using the <a href=https://opencrux.com/main/index.html>Crux</a> unbundled store.

Start the store by deploying Zookeeper, Kafka and the above service to kubernetes, as follows:

1. <code>kubectl create -f k8s/zookeeper-deployment.yml</code>
2. <code> kubectl create -f k8s/kafka-deployment.yml</code>
3. <code> kubectl create -f k8s/transaction-service-deployment.yml </code>

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
kafka-broker0-56545d949b-hh54l                1/1     Running   0          18h
zookeeper-deployment-1-6d9649f6cf-v7kph       1/1     Running   0          18h
</pre>

Once everything us up and running it should be possible to see the swagger api by going visting
<a href=http://localhost:32000/index.html#/>http://localhost:32000/index.html#/</a>
