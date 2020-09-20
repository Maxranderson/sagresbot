package sagresbot.dataproviders

import sagresbot.core.UserStatusChangeRepository
import sagresbot.core.entity.{User, UserStatusChange}

import scala.collection.mutable
import scala.util.Try

class UserStatusChangeInMemoryRepository(data: mutable.HashSet[UserStatusChange] = mutable.HashSet()) extends UserStatusChangeRepository {
  override def findLastUserStatusChange(user: User): Try[Option[UserStatusChange]] =
    Try(data.find(_.user == user))

  override def create(userStatusChange: UserStatusChange): Try[UserStatusChange] = Try {
    data += userStatusChange
    userStatusChange
  }

  override def listByUser(user: User): Try[Seq[UserStatusChange]] =
    Try(data.filter(_.user == user).toSeq)
}
