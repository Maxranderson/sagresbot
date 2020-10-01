package sagresbot.dataproviders

import DBConfiguration.ctx._
import sagresbot.core.UserStatusRepository
import sagresbot.core.entity.UserStatus
import sagresbot.dataproviders.schemas.UserStatusSchema._
import scala.util.Try

class UserStatusSqlLiteRepository() extends UserStatusRepository {

  override def listAll(): Try[Seq[UserStatus]] = Try {
    run(usersStatus)
  }

  override def create(userStatus: UserStatus): Try[UserStatus] = Try {
    run(usersStatus.insert(lift(userStatus)))
    userStatus
  }

  override def findByName(name: String): Try[Option[UserStatus]] = Try {
    run(usersStatus.filter(_.name == lift(name))).headOption
  }
}
