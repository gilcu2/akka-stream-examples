
import akka.stream._
import akka.stream.scaladsl._

import akka.{NotUsed, Done}
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths

object Numbers {

  def main(args: Array[String]) = {

    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val source: Source[Int, NotUsed] = Source(1 to 100)

    source.runForeach(i => println(i))(materializer)

    val done: Future[Done] = source.runForeach(i => println(i))(materializer)

    implicit val ec = system.dispatcher
    done.onComplete(_ => system.terminate())
  }
}
