
import java.time.Instant

import model.JwtValidWith
import org.scalatest.{Inside}
import org.scalatest.flatspec.AnyFlatSpec
import pdi.jwt.JwtAlgorithm
import service.JwtService

class JwtServiceTest extends AnyFlatSpec with JwtService with Inside {

  override val key = "secretKey"
  override val jwtAlgorithm = JwtAlgorithm.HS256
  val oneDayInSeconds = 86400
  val author = "John"

  it should "set claims with expiration, issue time and author" in {
    val claim = setClaimsWith(author, oneDayInSeconds)
    val expectedAuthor = author

    assert(claim.subject == Some(expectedAuthor))
    assert(claim.expiration.exists(_ > Instant.now.getEpochSecond))
    assert(claim.issuedAt.exists(_ <= Instant.now.getEpochSecond))
  }

  it should "generate Jwt string" in {
    val claim = setClaimsWith(author, oneDayInSeconds)
    val expectedJwtLength = 145
    val jwt = createJwtWithClaim(claim)

    assert(jwt.length == expectedJwtLength)
  }

  it should "be able to validate Jwt and retrieve `author` from it" in {
    val claim = setClaimsWith(author, oneDayInSeconds)
    val jwt = createJwtWithClaim(claim)
    val expectedAuthor = author

    inside(validateJwt(jwt)) { case JwtValidWith(author) =>
      assert(author == expectedAuthor)
    }
  }
}
