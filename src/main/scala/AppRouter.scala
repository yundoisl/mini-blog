
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.{ActorMaterializer, IOResult}

import Utils._


class AppRouter(materializer: ActorMaterializer) extends Router with Directives {

  override def route: Route = path("generatepassword") {
    get {
      parameters('author.as[String]) { (author) =>
        complete {
          generatePasswordFor(author)
        }
      }
    }
  }
}
