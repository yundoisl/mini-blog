package repository

import scala.concurrent.{ExecutionContext, Future}

trait BlogRepository {
  def createAuthor(author: String): Future[String]
  def createCard(name: String, content: String, category: String, status: String, author: String): Future[Int]
  def existsLoginCredentials(author: String, password: String): Future[Boolean]
  def updateCard(id: Int, name: String, status: String, content: String, category: String, author: String): Future[Boolean]
  def deleteCard(id: Int, author: String): Future[Boolean]
}
