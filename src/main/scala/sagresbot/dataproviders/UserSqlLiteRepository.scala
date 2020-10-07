package sagresbot.dataproviders

import java.time.LocalDateTime
import sagresbot.core.UserRepository
import sagresbot.core.entity.{User, UserStatus}
import DBConfiguration.ctx._
import io.getquill.Query
import sagresbot.dataproviders.DBConfiguration.ctx
import sagresbot.dataproviders.daos.UserDAO
import sagresbot.dataproviders.schemas.UserSchema._
import sagresbot.dataproviders.schemas.{UserSchema, UserStatusChangeSchema, UserStatusSchema}
import scala.util.Try

class UserSqlLiteRepository() extends UserRepository {

  private val lastStatusUpdateAtQuery: ctx.Quoted[Query[(Long, Option[LocalDateTime])]] = quote {
    UserStatusChangeSchema.usersStatusChanges
      .groupBy(_.userId)
      .map { case (userId, q) => (userId, q.map(_.updatedAt).max) }
  }

  private val lastStatusChangeQuery = quote {
    for {
      statusChange <- UserStatusChangeSchema.usersStatusChanges
      (userId, lastUpdateAt) <- lastStatusUpdateAtQuery
      if statusChange.userId == userId && lastUpdateAt.exists(_ == statusChange.updatedAt)
    } yield statusChange
  }

  private val lastChangeWithStatus = quote {
    for {
      change <- lastStatusChangeQuery
      status <- UserStatusSchema.usersStatus if change.statusName == status.name
    } yield (change, status)
  }

  private val listAllWithLastStatusQuery = quote {
    UserSchema.users
      .leftJoin(lastChangeWithStatus)
      .on(_.id == _._1.userId)
      .map(q => (q._1, q._2.map(_._2)))
      .nested
  }

  override def listAll(): Try[Seq[User]] = Try {
    run {
      users
    }.map(UserDAO.toUser(_, None))
  }

  override def findByFirstNameWithLastStatus(firstName: String): Try[Option[User]] = Try {
    run(listAllWithLastStatusQuery.filter(_._1.firstName == lift(firstName)))
      .headOption
      .map(t => UserDAO.toUser(t._1, t._2))
  }

  override def create(user: User): Try[User] = Try {
    run {
      users.insert(
        lift(UserDAO.fromUser(user))
      )
    }
    user
  }

  override def update(user: User): Try[User] = Try {
    run {
      users.filter(_.id == lift(user.id)).update(
        lift(UserDAO.fromUser(user))
      )
    }
    user
  }

  override def listAllWithLastStatus(): Try[Seq[User]] = Try {
    run(listAllWithLastStatusQuery).map(t => UserDAO.toUser(t._1, t._2))
  }
}
