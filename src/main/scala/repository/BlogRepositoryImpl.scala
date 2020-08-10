package repository

import java.sql.ResultSet

import postgresql.Datasource
import repository.Types._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Using}

class BlogRepositoryImpl extends BlogRepository {

  def executeQuery(sql: String): Either[RuntimeException, ResultSet] = {
    Using(Datasource.getConnection()) { connection =>
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(sql)
      resultSet.next()
      resultSet
    } match {
      case Success(resultSet) => Right(resultSet)
      case Failure(exception) => Left(new RuntimeException(exception))
    }
  }

  def executeUpdate(sql: String): Either[RuntimeException, RowCounts] = {
    Using(Datasource.getConnection()) { connection =>
      val statement = connection.createStatement()
      statement.executeUpdate(sql)
    } match {
      case Success(updatedRows) => Right(updatedRows)
      case Failure(exception) => Left(throw new RuntimeException(exception))
    }
  }

  def createAuthor(author: String): Future[Either[RuntimeException, Password]] = {
    Future {
      val generatedPassword = new scala.util.Random(author.hashCode).nextInt(Int.MaxValue).toString
      val sql = s"INSERT INTO author VALUES(DEFAULT, '$author', '$generatedPassword');"
      executeUpdate(sql) match {
        case Right(_) => Right(generatedPassword)
        case Left(ex) => Left(ex)
      }
    }
  }

  def createCard(name: String, content: String, category: String, status: String, author: String): Future[Either[RuntimeException, CardId]] = {
    Future {
      val sql =
        s"""INSERT INTO card (name, status, content, category, author) VALUES
           |('$name', '$status', '$content', '$category', (SELECT id FROM author WHERE name = '$author'))
           |RETURNING id;""".stripMargin
      executeQuery(sql) match {
        case Right(resultSet) => Right(resultSet.getInt(1))
        case Left(ex) => Left(ex)
      }
    }
  }

  def existsLoginCredentials(author: String, password: String): Future[Either[RuntimeException, Boolean]] = {
    Future {
      val sql = s"SELECT COUNT(*) FROM author WHERE name = '$author' AND password = '$password';"
      executeQuery(sql) match {
        case Right(resultSet) => Right(resultSet.getBoolean(1))
        case Left(ex) => Left(ex)
      }
    }
  }

  def updateCard(id: Int, name: String, status: String, content: String, category: String, author: String): Future[Either[RuntimeException, IsUpdated]] = {
    Future {
      val sql =
        s"""UPDATE card
           |SET name = '$name', status = '$status', content = '$content', category = '$category'
           |FROM author
           |WHERE card.author = author.id AND card.id = $id AND author.name = '$author';""".stripMargin
      executeUpdate(sql) match {
        case Right(updatedRowCount) if updatedRowCount == 1 => Right(true)
        case Right(_) => Right(false)
        case Left(ex) => Left(ex)
      }
    }
  }

  def deleteCard(id: Int, author: String): Future[Either[RuntimeException, IsDeleted]] = {
    Future {
      val sql = s"""DELETE FROM card C
                   |USING author AS A
                   |WHERE A.id = C.author AND C.id = $id AND A.name = '$author';""".stripMargin
      executeUpdate(sql) match {
        case Right(updatedRowCount) if updatedRowCount == 1 => Right(true)
        case Right(_) => Right(false)
        case Left(ex) => Left(ex)
      }
    }
  }}
