import BlogTestHelper.createRequest
import akka.http.scaladsl.model.HttpMethods.{DELETE, GET, POST, PUT}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import controller.BlogController
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpecLike
import repository.BlogRepository

import scala.concurrent.Future

class BlogControllerTest
  extends AuthenticatedWithAuthor("John")
    with AnyFlatSpecLike
    with MockFactory
    with ScalatestRouteTest {

  val mockRepository = mock[BlogRepository]
  val controller = new BlogController(mockRepository)

  it should "successfully sign up a new author named `Foo` and return a generated password" in {
    val author = "Foo"
    val password = "1844546102"
    (mockRepository.createAuthor _).
      expects(author).
      returning(Future.successful(Right(password))).once()
    val request = createRequest(POST, "/signup", """{"author":"Foo"}""")

    request ~>  controller.signup ~> check {
      assert(status.isSuccess)
      assert(responseAs[String] == s"Your password has been generated: $password")
    }
  }

  it should "fail to sign up a new author named `Foo` and response with `InternalServerError` " in {
    val author = "Foo"
    (mockRepository.createAuthor _)
      .expects(author)
      .returning(Future.failed(new RuntimeException())).once()
    val request = createRequest(POST, "/signup", """{"author":"Foo"}""")

    request ~>  controller.signup ~> check {
      assert(!status.isSuccess)
      assert(responseAs[String].contains("An error occurred"))
    }
  }

  it should "successfully log in with valid credentials and return a response with jwt inside `X-Access-Token` header" in {
    val author = "Foo"
    val password = "1844546102"
    val authenticated = true
    (mockRepository.existsLoginCredentials _)
      .expects(author, password)
      .returning(Future.successful(Right(authenticated))).once()
    val request = createRequest(GET, "/login", """{ "author" : "Foo", "password": "1844546102"}""")

    request ~>  controller.login ~> check {
      assert(status.isSuccess)
      assert(responseAs[String] == "Successfully logged in")
    }
  }

  it should "create a new card with valid jwt" in {
    val cardName = "My First Card"
    val content = "Hi, World"
    val category = "journey"
    val cardStatus = "draft"
    val cardId = 0
    (mockRepository.createCard _)
      .expects(cardName, content, category, cardStatus, author)
      .returning(Future.successful(Right(cardId))).once()
    val request = createRequest(
      httpMethod = POST,
      uri = "/create",
      json = """{"name": "My First Card", "content": "Hi, World", "category": "journey", "status": "draft"}""",
      headers = Seq(authorizationHeader))

    request ~>  controller.create ~> check {
      assert(status.isSuccess)
      assert(responseAs[String].contains("Your card has been created with id"))
    }
  }

  it should "update a card with valid jwt" in {
    val id = 1
    val cardName = "My First Update"
    val content = "Updated"
    val category = "books"
    val cardStatus = "updated"
    val updated = true
    (mockRepository.updateCard _)
      .expects(id, cardName, content, category, cardStatus, author)
      .returning(Future.successful(Right(updated))).once()
    val request = createRequest(
      httpMethod = PUT,
      uri = "/update",
      json = """{"id": 1, "name": "My First Update", "content": "Updated", "category": "books", "status": "updated"}""",
      headers = Seq(authorizationHeader))

    request ~>  controller.update ~> check {
      assert(status.isSuccess)
      assert(responseAs[String].contains(s"Your card id: $id has been updated"))
    }
  }

  it should "delete a card with valid jwt" in {
    val id = 1
    val deleted = true
    (mockRepository.deleteCard _)
      .expects(id, author)
      .returning(Future.successful(Right(deleted))).once()

    val request = createRequest(
      httpMethod = DELETE,
      uri = "/delete",
      json = """{"id": 1}""",
      headers = Seq(authorizationHeader))

    request ~>  controller.delete ~> check {
      assert(status.isSuccess)
      assert(responseAs[String].contains(s"Your card id: $id has been deleted"))
    }
  }

}

//object BlogControllerTest {
////  def createPostRequest(uri: String, json: String, headers: Seq[HttpHeader] = Nil): HttpRequest = {
////    HttpRequest(
////      HttpMethods.POST,
////      uri = uri,
////      headers = headers,
////      entity = HttpEntity(MediaTypes.`application/json`, ByteString(json)))
////  }
//
//  def createRequest(httpMethod: HttpMethod, uri: String, json: String, headers: Seq[HttpHeader] = Nil): HttpRequest = {
//    HttpRequest(
//      HttpMethods.POST,
//      uri = uri,
//      headers = headers,
//      entity = HttpEntity(MediaTypes.`application/json`, ByteString(json)))
//  }
//}
