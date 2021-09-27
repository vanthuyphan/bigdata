package van.bd.cs523

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.cs523.dto._
import com.typesafe.config.ConfigFactory
import org.apache.commons.lang3.{RandomStringUtils, RandomUtils}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

object TweetProducer {

  val logger = LoggerFactory.getLogger(classOf[Streamer])

  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load
    val streamer = new Streamer(conf)

    val mapper = new ObjectMapper()
    mapper.registerModules(DefaultScalaModule, new JavaTimeModule())

    new Thread(new Runnable {
      override def run(): Unit = {
        while (true) {
          val items = new ArrayBuffer[Tweet]()
          val tweet = TweetFeeds.nextRandom()
          val item = Tweet(tweet("id"), tweet("content"), tweet("userId"), tweet("hashtag"))
          items += item
          val bundle = Bundle(
            UUID.randomUUID().toString,
            items.toList
          )
          streamer.sendTweet(mapper.writeValueAsString(bundle), bundle.bundleId.toString)
          Thread.sleep(50)
        }
      }
    }).start()
  }
}
