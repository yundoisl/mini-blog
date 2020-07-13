import postgresql.Datasource

import scala.util.Using

object Utils {
  def generatePasswordFor(author: String): String = {

    val generatedPassword = new scala.util.Random(author.hashCode).nextString(20)

    Using(Datasource.getConnection) { connection =>
      val statement = connection.createStatement()
      val sql = s"INSERT INTO author VALUES(DEFAULT, $author, $generatedPassword);"

      statement.execute(sql)
    }
  }
}
