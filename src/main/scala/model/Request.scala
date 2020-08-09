package model

sealed trait Request

final case class LoginRequest(author: String, password: String) extends Request
final case class SignUpRequest(author: String) extends Request
final case class CreateCardRequest(name: String, content: String, category: String, status: String) extends Request
final case class UpdateCardRequest(id: Int, name: String, content: String, category: String, status: String) extends Request
final case class DeleteCardRequest(id: Int) extends Request
