Final project for Big Data Course.
====================

* Hadoop 2.7.0
* Spark 2.4
* Kafka 2.0.2
* Cassandra 3.0.4
* Hive 2.3.2
* ZooKeeper 3.4.14

## Language and Build tools

* Scala
* Gradle (Kotlin)
* Docker and docker-compose

## How to run

* Run the script `start.sh` to start hsdf, kafka, spark, hive, zookeeper, etc
* Run producer `producer.sh` to write data to Kafka
Run spark-submit `spark-submit.sh` to submit a job to spark to consume data

## Verify data

### Verify via hive
Exec to hive and run query to check table tweets
```cmd
docker exec -it hive-metadata sh
```
### Verify via hdfs

``docker exec -it namenode bash``
, then list all files in the folder 
`/user/hive/warehouse/tweets` 

```cmd
hdfs dfs -ls /user/hive/warehouse/tweets
```

Notice
=======
 
I used [https://github.com/big-data-europe](https://github.com/big-data-europe) 
as a bootstrap project


