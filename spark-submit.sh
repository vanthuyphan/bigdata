#!/bin/bash

./gradlew shadowJar

docker cp stream-processing/build/libs/stream-processing-1.0-SNAPSHOT-all.jar spark-master:data

docker exec -ti spark-master sh -c  "cd data && /spark/bin/spark-submit --class com.bd.streaming.hive.TweetStreamingHiveApp --master spark://spark-master:7077 stream-processing-1.0-SNAPSHOT-all.jar"