package sagresbot.dataproviders

import sagresbot.core.UserRepository
import sagresbot.core.entity.User
import scala.collection.mutable
import scala.util.Try

class UserInMemoryRepository(data: mutable.HashSet[User] = mutable.HashSet()) extends UserRepository {
  override def listAll(): Try[Seq[User]] = Try(data.toSeq)

  override def findByFirstName(firstName: String): Try[Option[User]] =
    Try(data.find(_.firstName == firstName))

  override def create(user: User): Try[User] = Try {
    data += user
    user
  }

  override def update(user: User): Try[User] = Try {
    data.update(user, included = true)
    user
  }
}
