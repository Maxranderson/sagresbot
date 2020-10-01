package sagresbot.core

import sagresbot.core.entity.{User, UserStatusChange}

import scala.util.Try

trait UserStatusChangeRepository {

  def create(userStatusChange: UserStatusChange): Try[UserStatusChange]

  def listByUser(user: User): Try[Seq[UserStatusChange]]

}
