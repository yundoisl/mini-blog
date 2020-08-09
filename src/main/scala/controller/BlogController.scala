package controller

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode, StatusCodes}
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, optionalHeaderValueByName, provide, respondWithHeader}
import akka.http.scaladsl.server.{Directive1, Route}
import model.{CreateCardRequest, DeleteCardRequest, JwtExpiredError, JwtNotValidError, JwtValidWith, LoginRequest, SignUpRequest, UpdateCardRequest}
import pdi.jwt.JwtAlgorithm
import io.circe.generic.auto.exportDecoder
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import repository.BlogRepository
import service.JwtService

import scala.util.{Failure, Success}

class BlogController(repository: BlogRepository) extends JwtService {

  override val key = "secretKey"
  override val jwtAlgorithm = JwtAlgorithm.HS256
  val oneDayInSeconds = 86400

  def login: Route = entity(as[LoginRequest]) {
    case LoginRequest(author, password) =>
      onComplete(repository.existsLoginCredentials(author, password)) {
        case Success(isAuthenticated) => {
          if (isAuthenticated) {
            val jwt = createJwtWithClaim(setClaimsWith(author, oneDayInSeconds))
            val AccessTokenHeaderName = "X-Access-Token"
            respondWithHeader(RawHeader(AccessTokenHeaderName, jwt)) {
              completeWith(StatusCodes.OK, "Successfully logged in")
            }
          } else {
            completeWith(StatusCodes.Unauthorized, "Authentication failed")
          }
        }
        case Failure(ex) =>
          completeWith(StatusCodes.InternalServerError, s"An error occurred : ${ex.getMessage}")
      }
  }

  def signup: Route = entity(as[SignUpRequest]) {
    case SignUpRequest(author) => {
      onComplete(repository.createAuthor(author)) {
        case Success(password) =>
          completeWith(StatusCodes.OK, s"Your password has been generated: $password")
        case Failure(ex) =>
          completeWith(StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}")
      }
    }
  }

  def create: Route = authenticated { author =>
    println("author : " + author)
    entity(as[CreateCardRequest]) {
      case CreateCardRequest(name, content, category, status) =>
        onComplete(repository.createCard(name, content, category, status, author)) {
          case Success(resultId) =>
            completeWith(StatusCodes.OK, s"Your card has been created with id : $resultId")
          case Failure(ex) =>
            completeWith(StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
    }
  }

  def update: Route = authenticated { author =>
    entity(as[UpdateCardRequest]) {
      case UpdateCardRequest(id, name, content, category, status) =>
        onComplete(repository.updateCard(id, name, content, category, status, author)) {
          case Success(true) =>
            completeWith(StatusCodes.OK,s"Your card $id has been updated")
          case Success(false) =>
            completeWith(StatusCodes.Unauthorized, s"Given card $id does not exist or you are not the owner of the card")
          case Failure(ex) =>
            completeWith(StatusCodes.InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
    }
  }

  def delete: Route = authenticated { author =>
    entity(as[DeleteCardRequest]) {
      case DeleteCardRequest(id) =>
        onComplete(repository.deleteCard(id, author)) {
          case Success(true) =>
            completeWith(StatusCodes.OK,s"Your card $id has been deleted")
          case Success(false) =>
            completeWith(StatusCodes.Unauthorized,"The card: $id does not exist or you are not the owner of the card")
          case Failure(ex) =>
            completeWith(StatusCodes.InternalServerError,s"An error occurred: ${ex.getMessage}")
        }
    }
  }

  def authenticated: Directive1[String] =
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(token) => validateJwt(token) match {
        case JwtNotValidError => completeWith(StatusCodes.Unauthorized,"JWT is not valid.")
        case JwtExpiredError => completeWith(StatusCodes.Unauthorized,"Session expired.")
        case JwtValidWith(author) => provide(author)
      }
      case None => completeWith(StatusCodes.Unauthorized, "JWT is not provided.")
    }

  def completeWith(statusCode: StatusCode, message: String) = {
    complete(statusCode -> HttpEntity(ContentTypes.`text/plain(UTF-8)`, message))
  }
}
