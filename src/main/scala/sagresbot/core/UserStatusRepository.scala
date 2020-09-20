package sagresbot.core

import sagresbot.core.entity.UserStatus
import scala.util.Try

trait UserStatusRepository {

  def listAll(): Try[Seq[UserStatus]]

  def create(userStatus: UserStatus): Try[UserStatus]

  def findByName(name: String): Try[Option[UserStatus]]
}
