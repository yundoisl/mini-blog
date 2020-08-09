import java.io.{File}

import BlogControllerTest.createPostRequest
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.stream.ActorMaterializer
import com.dimafeng.testcontainers.{DockerComposeContainer, ExposedService, ForAllTestContainer}
import org.scalatest.flatspec.{AsyncFlatSpec}
import pdi.jwt.JwtAlgorithm
import service.JwtService

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class DockerComposeIntegrationTest extends AsyncFlatSpec with JwtHelperForTest with ForAllTestContainer {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher
  val blogAppServiceName = "mini-blog_1"
  val blogAppPort = 9000

  override val container =
    DockerComposeContainer(
      composeFiles = new File("src/test/resources/docker-compose.yml"),
      exposedServices = Seq(ExposedService(blogAppServiceName, blogAppPort))
    )
  container.start()

  val port = container.getServicePort(blogAppServiceName, blogAppPort)
  val host = container.getServiceHost(blogAppServiceName, blogAppPort)

  behavior of "mini blog app"

  it should "successfully log in with valid login credentials" in {
    val expectedText = "Successfully logged in"
    val requestBody = """{ "author" : "test", "password": "1033386069"}"""
    val requestUri = s"http://$host:$port/login"
    val request = createPostRequest(requestUri, requestBody)
    val responseFuture = Http().singleRequest(request)

    responseFuture.flatMap(_.entity.toStrict(2 seconds)).map(_.data.utf8String).map { msg =>
      assert(msg == expectedText)
    }
  }

  it should "successfully sign up a new author and response back with the generated password" in {
    val expectedText = "Your password has been generated: 1901348939"
    val requestBody = """{ "author" : "Foo" }"""
    val requestUri = s"http://$host:$port/signup"
    val request = createPostRequest(requestUri, requestBody)

    val responseFuture = Http().singleRequest(request)
    responseFuture.flatMap(_.entity.toStrict(2 seconds)).map(_.data.utf8String).map { msg =>
      assert(msg == expectedText)
    }
  }

  it should "successfully create new card as `test` author" in {
    val expectedText = "Your card has been created with id"
    val requestBody = """{"name": "My First Card", "content": "Hi, World", "category": "journey", "status": "draft"}"""
    val requestUri = s"http://$host:$port/create"
    val request = createPostRequest(requestUri, requestBody, headers = Seq(authorizationHeader))

    val responseFuture = Http().singleRequest(request)
    responseFuture.flatMap(_.entity.toStrict(2 seconds)).map(_.data.utf8String).map { msg =>
      assert(msg.contains(expectedText))
    }
  }

  it should "succesfully update an existing card owned by `test` author" in {
    val expectedText = "Your card 1 has been updated"
    val requestBody = """{"id": 1, "name": "My First Update", "content": "Updated", "category": "books", "status": "updated"}"""
    val requestUri = s"http://$host:$port/update"
    val request = createPostRequest(requestUri, requestBody, headers = Seq(authorizationHeader))

    val responseFuture = Http().singleRequest(request)
    responseFuture.flatMap(_.entity.toStrict(2 seconds)).map(_.data.utf8String).map { msg =>
      assert(msg == expectedText)
    }
  }

  it should "successfully delete a card owned by `test` author" in {
    val expectedText = "Your card 1 has been deleted"
    val requestBody = """{"id": 1}"""
    val requestUri = s"http://$host:$port/delete"
    val request = createPostRequest(requestUri, requestBody, headers = Seq(authorizationHeader))

    val responseFuture = Http().singleRequest(request)
    responseFuture.flatMap(_.entity.toStrict(2 seconds)).map(_.data.utf8String).map { msg =>
      assert(msg == expectedText)
    }
  }
}

trait JwtHelperForTest extends JwtService {
  override val key = "secretKey"
  override val jwtAlgorithm = JwtAlgorithm.HS256
  val oneDayInSeconds = 86400
  val author = "test"
  val claim = setClaimsWith(author, oneDayInSeconds)
  val generatedJwt = createJwtWithClaim(claim)
  val authorizationHeader = new RawHeader("Authorization", generatedJwt)
}

