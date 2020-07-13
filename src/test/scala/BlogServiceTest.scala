import com.typesafe.scalalogging.Logger
import org.scalatest.flatspec.AsyncFlatSpec

class BlogServiceTest extends AsyncFlatSpec {

  val logger: Logger = Logger("UtilsTest")

  behavior of "creating card"

  it should "create card and return the row id" in {
    val name = "name"
    val content = "this is the content"
    val category = "some random category"
    val author = "author"

    blogService.createCard(name, content, category, author).map { id =>
      logger.info("id : " + id)

      assert(id > 0)
    }
  }

  behavior of "authenticating an user"

  it should "return True because the user is already registered" in {
    val author = "test"
    val password = "炴㲮鯸쵇厳夗ₙ㒞즞혅듞儠鮈馽ᯑ땶슚鬧㔕Ḥ"
    blogService.authenticateAuthor(author, password). map { isUserRegistered =>

      assert(isUserRegistered == true)
    }
  }

  it should "return False because such user does not exist" in {
    val author = "nonExistent"
    val password = "nonExistent"
    blogService.authenticateAuthor(author, password). map { isUserRegistered =>

      assert(isUserRegistered == false)
    }
  }
}
