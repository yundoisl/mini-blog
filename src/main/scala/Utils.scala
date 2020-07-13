import com.typesafe.scalalogging.Logger
import postgresql.Datasource

import scala.util.{Failure, Success, Using}
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object Utils {

  val logger: Logger = Logger("Utils")

  def generatePasswordFor(author: String): Future[String] = {
    Future {
      val generatedPassword = new scala.util.Random(author.hashCode).nextString(20)
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
}
