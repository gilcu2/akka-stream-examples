
import akka.stream._
import akka.stream.scaladsl._

import akka.{NotUsed, Done}
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths

object Throttle {

  def main(args: Array[String]) = {

    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val source: Source[Int, NotUsed] = Source(1 to 100)

    val factorials = source.scan(BigInt(1))((acc, next) => acc * next) // Combinator

    val result =
      factorials
        .zipWith(Source(0 to 100))((num, idx) => s"$idx! = $num")
        .throttle(1, 1.second, 1, ThrottleMode.shaping) // slow down the stream to 1 element per second
        .runForeach(println)

    implicit val ec = system.dispatcher
    result.onComplete(_ => system.terminate())
  }
}
