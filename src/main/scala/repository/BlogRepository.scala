package repository

import repository.Types.{CardId, IsDeleted, IsUpdated, Password}

import scala.concurrent.Future

trait BlogRepository {
  def createAuthor(author: String): Future[Either[RuntimeException, Password]]
  def createCard(name: String, content: String, category: String, status: String, author: String): Future[Either[RuntimeException, CardId]]
  def existsLoginCredentials(author: String, password: String): Future[Either[RuntimeException, Boolean]]
  def updateCard(id: Int, name: String, status: String, content: String, category: String, author: String): Future[Either[RuntimeException, IsUpdated]]
  def deleteCard(id: Int, author: String): Future[Either[RuntimeException, IsDeleted]]
}
