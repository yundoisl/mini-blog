import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.{ExecutionContext, Future}
import router.Router

class Server(router: Router, host: String, port: Int)(
    implicit system: ActorSystem,
    ec: ExecutionContext,
    mat: Materializer) {

  def bind(): Future[ServerBinding] =
    Http().bindAndHandle(router.route, host, port)

}
