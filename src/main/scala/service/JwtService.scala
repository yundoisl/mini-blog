package service

import java.time.Instant

import model.{JwtExpiredError, JwtNotValidError, JwtValidWith, JwtValidationResult}
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import scala.util.{Failure, Success}

trait JwtService {

  def key: String
  def jwtAlgorithm: JwtAlgorithm

  def setClaimsWith(author: String, expirationTimeInSec: Int): JwtClaim = {
    JwtClaim(
      expiration = Some(Instant.now.plusSeconds(expirationTimeInSec).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond),
      subject = Some(author))
  }

  def createJwtWithClaim(claim: JwtClaim): String = {
    JwtCirce.encode(claim, key, jwtAlgorithm)
  }

  def validateJwt(token: String): JwtValidationResult = {
    JwtCirce.decodeJson(token, key, Seq(JwtAlgorithm.HS256)) match {
      case Success(json) => {
        val expirationFieldDecodedResult = json.hcursor.downField("exp").as[Long]
        val authorFieldDecodedResult = json.hcursor.downField("sub").as[String]
        (expirationFieldDecodedResult, authorFieldDecodedResult) match {
          case (Right(expiration), _) if expiration < Instant.now.getEpochSecond => JwtExpiredError
          case (Right(_), Right(author)) => JwtValidWith(author)
          case _ => JwtNotValidError
        }
      }
      case Failure(_) => JwtNotValidError
    }
  }
}
