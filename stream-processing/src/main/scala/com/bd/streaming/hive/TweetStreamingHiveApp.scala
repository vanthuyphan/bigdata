package com.bd.streaming.hive

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.cs523.dto.Tweet
import com.cs523.dto.Bundle
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ArrayBuffer

object TweetStreamingHiveApp {
  def jsonMapper(): ObjectMapper = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModules(DefaultScalaModule, new JavaTimeModule())
    mapper
  }

  def main(args: Array[String]): Unit = {
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "kafka:9093",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val spark = SparkSession.builder()
      .appName("Tweet Streaming Processing")
      .master("local[*]")
      .config("spark.driver.allowMultipleContexts", "true")
      .config("hive.metastore.uris", "thrift://hive-metastore:9083")
      .config("spark.sql.warehouse.dir", "hdfs://namenode:9000/hive")
      .enableHiveSupport()
      .getOrCreate()

    val sqlContext = spark.sqlContext
    val ssc = new StreamingContext(spark.sparkContext, Seconds(15))
    val topics = Array("tweets-topic")

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    sqlContext.sql("CREATE TABLE IF NOT EXISTS tweets (tweetId String, content STRING, userId STRING) USING HIVE")

    stream.map(record => record.value()).map(value => jsonMapper().readValue(value, classOf[Bundle]))
      .map(bundle => {
        val result = new ArrayBuffer[TweetRecord]()
        val tweets = bundle.tweets
        tweets.foreach(tweet => {
          result += TweetRecord(tweet.tweetId, tweet.content, tweet.userId)
        })
        result
      }).foreachRDD(rdd => {
      import sqlContext.implicits._
      rdd.flatMap(item => item.toList).toDF().write.mode(SaveMode.Append).format("hive")
        .saveAsTable("tweets")
    })

    ssc.start() // Start the computation
    ssc.awaitTermination() // Wait for the computation to terminate
  }
}
