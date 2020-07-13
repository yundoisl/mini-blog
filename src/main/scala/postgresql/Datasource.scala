package postgresql

import java.sql.{Connection, DriverManager}

import com.typesafe.scalalogging.Logger

object Datasource {

  val logger = Logger("Main")

  val db = "mini-blog-db"
  val host = "localhost"
  val url = s"jdbc:postgresql://${host}:5432/${db}"
  val user = "mini-blog-user"
  val password = "postgres1234"

  def getConnection(): Connection = DriverManager.getConnection(url, user, password)

}
