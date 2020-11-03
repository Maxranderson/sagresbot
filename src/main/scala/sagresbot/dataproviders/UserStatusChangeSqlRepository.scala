package sagresbot.dataproviders

import sagresbot.core.UserStatusChangeRepository
import sagresbot.core.entity.{User, UserStatusChange}
import DBConfiguration.ctx._
import sagresbot.dataproviders.daos.{UserDAO, UserStatusChangeDAO}
import sagresbot.dataproviders.schemas.{UserSchema, UserStatusSchema}
import sagresbot.dataproviders.schemas.UserStatusChangeSchema._
import scala.util.Try

class UserStatusChangeSqlRepository() extends UserStatusChangeRepository {

  override def create(userStatusChange: UserStatusChange): Try[UserStatusChange] = Try {
    run(usersStatusChanges.insert(
      lift(UserStatusChangeDAO(userStatusChange.user.id, userStatusChange.status.name, userStatusChange.updatedAt))
    ))
    userStatusChange
  }

  override def listByUser(user: User): Try[Seq[UserStatusChange]] = Try {
    run {
      for {
        statusChange <- usersStatusChanges
        user <- UserSchema.users if statusChange.userId == user.id
        status <- UserStatusSchema.usersStatus if statusChange.statusName == status.name
      } yield (user, status, statusChange.updatedAt)
    }.map(t => UserStatusChange(UserDAO.toUser(t._1, Some(t._2)), t._2, t._3))
  }
}
