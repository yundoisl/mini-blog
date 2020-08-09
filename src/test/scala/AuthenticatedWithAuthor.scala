import akka.http.scaladsl.model.headers.RawHeader
import pdi.jwt.JwtAlgorithm
import service.JwtService

abstract class AuthenticatedWithAuthor(val author: String) extends JwtService {
  override val key = "secretKey"
  override val jwtAlgorithm = JwtAlgorithm.HS256
  val oneDayInSeconds = 86400
  val claim = setClaimsWith(author, oneDayInSeconds)
  val generatedJwt = createJwtWithClaim(claim)
  val authorizationHeader = new RawHeader("Authorization", generatedJwt)
}

