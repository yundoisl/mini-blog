package router

import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.Materializer
import controller.BlogController

class AppRouter(blogController: BlogController)
               (implicit materializer: Materializer) extends Router with Directives {

  override def route: Route =
    concat(
      path("login") {
        get {
          blogController.login
        }
      },
      path("create") {
        post {
          blogController.create
        }
      },
      path("update") {
        put {
          blogController.update
        }
      },
      path("delete") {
        delete {
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
