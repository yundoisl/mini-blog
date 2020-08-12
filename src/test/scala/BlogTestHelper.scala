import akka.http.scaladsl.model._
import akka.util.ByteString

object BlogTestHelper {
  def createRequest(httpMethod: HttpMethod, uri: String, json: String, headers: Seq[HttpHeader] = Nil): HttpRequest = {
    HttpRequest(
      httpMethod,
      uri = uri,
      headers = headers,
      entity = HttpEntity(MediaTypes.`application/json`, ByteString(json)))
  }
}
