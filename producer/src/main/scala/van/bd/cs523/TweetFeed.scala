package van.bd.cs523
import com.cs523.dto._

import org.apache.commons.lang3.RandomUtils
object TweetFeeds {
  val tweets = List(
    Map("id" -> "1", "content" -> "Apple Iphone is so overrated", "userId" -> "vanthuyphan@gmail.com", "hashtag" -> "#apple"),
    Map("id" -> "2", "content" -> "Apple Iphone is awesome", "userId" -> "vanthuyphan@gmail.com", "hashtag" -> "#cool")
  )

  def nextRandom(): Map[String, String] = {
    val index = RandomUtils.nextInt(0, tweets.size)
    tweets(RandomUtils.nextInt(0, tweets.size))
  }
}
