object Utils {
  def generatePasswordFor(author: String): String = {
    new scala.util.Random(author.hashCode).nextString(20)

  }
}
