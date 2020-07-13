import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.Logger

import scala.concurrent.Await
import scala.util.{Failure, Success}

object Main extends App {
  val host = "0.0.0.0"
  val port = 9000

  implicit val system: ActorSystem = ActorSystem(name = "mini-blog")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  import system.dispatcher

  val router = new AppRouter(materializer)

  val server = new Server(router, host, port)

  val logger = Logger("Main")

  val binding = server.bind()
  binding.onComplete {
    case Success(_)     => logger.info("Success!")
    case Failure(error) => logger.info(s"Failed: ${error.getMessage}")
  }

  import scala.concurrent.duration._
  Await.result(binding, 3.seconds)
}
