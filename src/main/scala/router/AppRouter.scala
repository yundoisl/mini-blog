package router

import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.{ActorMaterializer, Materializer}
import controller.BlogController

class AppRouter(blogController: BlogController)
               (implicit materializer: Materializer) extends Router with Directives {

  override def route: Route =
    concat(
      path("login") {
        post {
          blogController.login
        }
      },
      path("create") {
        post {
          blogController.create
        }
      },
      path("update") {
        post {
          blogController.update
        }
      },
      path("delete") {
        post {
          blogController.delete
        }
      },
      path("signup") {
        post {
          blogController.signup
        }
      }
    )
}
