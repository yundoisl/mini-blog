package model

sealed trait JwtValidationResult

final case object JwtNotValidError extends JwtValidationResult
final case object JwtExpiredError extends JwtValidationResult
final case class JwtValidWith(author: String) extends JwtValidationResult
