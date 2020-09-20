package sagresbot.dataproviders

import sagresbot.core.UserStatusRepository
import sagresbot.core.entity.{UserStatus, UserStatusChange}

import scala.collection.mutable
import scala.util.Try

class UserStatusInMemoryRepository(data: mutable.HashSet[UserStatus] = mutable.HashSet()) extends UserStatusRepository {
  def create(userStatus: UserStatus): Try[UserStatus] = Try {
    data += userStatus
    userStatus
  }

  def listAll(): Try[Seq[UserStatus]] =
    Try(data.toSeq)

  override def findByName(name: String): Try[Option[UserStatus]] =
    Try(data.find(_.name == name))
}
