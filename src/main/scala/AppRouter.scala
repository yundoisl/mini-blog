import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.{ActorMaterializer, IOResult}
import Utils._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class AppRouter(materializer: ActorMaterializer)
    extends Router
    with Directives {

  override def route: Route =
    concat(path("signup") {
      post {
        parameters('author.as[String]) {
          (author) =>
            val signUpResult = signUp(author)

            onComplete(signUpResult) {
              case Success(password) =>
                complete(
                  HttpEntity(
                    ContentTypes.`text/plain(UTF-8)`,
                    s"Your password has been generated : $password\n"
                  )
                )
              case Failure(ex) =>
                complete(
                  (
                    StatusCodes.InternalServerError,
                    s"An error occurred: ${ex.getMessage}\n"
                  )
                )

            }
        }
      }
    }, path("create") {
      post {
        parameters(
          'name.as[String],
          'content.as[String],
          'category.as[String],
          'author.as[String],
          'password.as[String]
        ) {
          (name, content, category, author, password) =>
            val cardCreationResult = authenticateAuthor(author, password)
              .flatMap { isAuthenticated =>
                if (isAuthenticated) {
                  createCard(name, content, category, author)
                } else {
                  throw new RuntimeException("user does not exist\n")
                }
              }

            onComplete(cardCreationResult) {
              case Success(resultId) =>
                complete(
                  HttpEntity(
                    ContentTypes.`text/plain(UTF-8)`,
                    s"Your card has been created with id : $resultId\n"
                  )
                )
              case Failure(ex) =>
                complete(
                  (
                    StatusCodes.InternalServerError,
                    s"An error occurred: ${ex.getMessage}"
                  )
                )
            }
        }
      }
    })
}
