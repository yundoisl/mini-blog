import com.typesafe.scalalogging.Logger
import postgresql.Datasource

import scala.util.{Failure, Success, Using}
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object blogService {

  val logger: Logger = Logger("Blog Service")

  def signUp(author: String): Future[String] = {
    Future {
      val generatedPassword = new scala.util.Random(author.hashCode).nextInt(Int.MaxValue).toString
      Using(Datasource.getConnection()) { connection =>
        val statement = connection.createStatement()
        val sql = s"INSERT INTO author VALUES(DEFAULT, '$author', '$generatedPassword');"
        statement.execute(sql)
      } match {
        case Success(_) => generatedPassword
        case Failure(exception) => throw new RuntimeException(exception)
      }
    }
  }

  def authenticateAuthor(author: String, password: String): Future[Boolean] = {
    Future {
      Using(Datasource.getConnection()) { connection =>
        val statement = connection.createStatement()
        val sql = s"SELECT COUNT(*) FROM author WHERE name = '$author' AND password = '$password';"
        logger.info("sql : " + sql)
        val resultSet = statement.executeQuery(sql)
        resultSet.next()
        resultSet.getBoolean(1)
      } match {
        case Success(result) => result
        case Failure(exception) => throw new RuntimeException(exception)
      }
    }
  }

  def createCard(name: String, content:String, category:String, author:String): Future[Int] = {
    Future {
      Using(Datasource.getConnection()) { connection =>
        val statement = connection.createStatement()
        val createdStatus = "created"
        val sql = s"""INSERT INTO card (name, status, content, category, author) VALUES
                     |('$name', '$createdStatus', '$content', '$category', (SELECT id FROM author WHERE name = '$author'))
                     |RETURNING id;""".stripMargin
        val resultSet = statement.executeQuery(sql)
        resultSet.next()
        resultSet.getInt(1)
      } match {
        case Success(cardId) => cardId
        case Failure(exception) => throw new RuntimeException(exception)
      }
    }
  }
}
