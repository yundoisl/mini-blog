import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer, SystemMaterializer}
import com.typesafe.scalalogging.Logger
import controller.BlogController
import repository.{BlogRepository, BlogRepositoryImpl}
import router.AppRouter

import scala.concurrent.Await
import scala.util.{Failure, Success}

object Main extends App {
  val host = "0.0.0.0"
  val port = 9000
  implicit val system: ActorSystem = ActorSystem(name = "mini-blog")
  implicit def executor = system.dispatcher
  implicit val materializer = SystemMaterializer(system).materializer
  val blogController = new BlogController(new BlogRepositoryImpl())
  val router = new AppRouter(blogController)
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
