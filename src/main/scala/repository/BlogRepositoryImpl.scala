package repository

import java.sql.ResultSet

import com.typesafe.scalalogging.Logger
import postgresql.Datasource

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Using}

class BlogRepositoryImpl extends BlogRepository {

  val logger: Logger = Logger("Blog Service")

  def executeQuery(sql: String): ResultSet = {
    Using(Datasource.getConnection()) { connection =>
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(sql)
      resultSet.next()
      resultSet
    } match {
      case Success(resultSet) => resultSet
      case Failure(exception) => throw new RuntimeException(exception)
    }
  }

  def executeUpdate(sql: String): Int = {
    Using(Datasource.getConnection()) { connection =>
      val statement = connection.createStatement()
      statement.executeUpdate(sql)
    } match {
      case Success(updatedRows) => updatedRows
      case Failure(exception) => {
        exception.printStackTrace() //TODO : remove
        throw new RuntimeException(exception)
      }
    }
  }

  def createAuthor(author: String): Future[String] = {
    Future {
      // TODO: (Note) generate a random pw based on the given username `author`, so `author` is the seed for the Random function, for every single `author` same password will be generated because it is using the same seed
      // for same strings same hashCode will be generated
      val generatedPassword = new scala.util.Random(author.hashCode).nextInt(Int.MaxValue).toString
      val sql = s"INSERT INTO author VALUES(DEFAULT, '$author', '$generatedPassword');"
      executeUpdate(sql)
      generatedPassword
    }
  }

  def createCard(name: String, content: String, category: String, status: String, author: String): Future[Int] = {
    Future {
      val sql =
        s"""INSERT INTO card (name, status, content, category, author) VALUES
           |('$name', '$status', '$content', '$category', (SELECT id FROM author WHERE name = '$author'))
           |RETURNING id;""".stripMargin
      executeQuery(sql).getInt(1)
    }
  }

  def existsLoginCredentials(author: String, password: String): Future[Boolean] = {
    Future {
      val sql = s"SELECT COUNT(*) FROM author WHERE name = '$author' AND password = '$password';"
      executeQuery(sql).getBoolean(1)
    }
  }

  def updateCard(id: Int, name: String, status: String, content: String, category: String, author: String): Future[Boolean] = {
    Future {
      val sql =
        s"""UPDATE card
           |SET name = '$name', status = '$status', content = '$content', category = '$category'
           |FROM author
           |WHERE card.author = author.id AND card.id = $id AND author.name = '$author';""".stripMargin
      val updatedRowCount = executeUpdate(sql)
      updatedRowCount match {
        case 1 => true
        case _ => false
      }
    }
  }

  def deleteCard(id: Int, author: String): Future[Boolean]= {
    Future {
      val sql = s"""DELETE FROM card C
                   |USING author AS A
                   |WHERE A.id = C.author AND C.id = $id AND A.name = '$author';""".stripMargin
      val updatedRowCount = executeUpdate(sql)
      updatedRowCount match {
        case 1 => true
        case _ => false
      }
    }
  }}
