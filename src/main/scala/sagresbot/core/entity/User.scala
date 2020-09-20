package sagresbot.core.entity

import java.time.{Clock, LocalDateTime}

case class User(id: Long,
                isBot: Boolean,
                firstName: String,
                lastName: Option[String],
                username: Option[String],
                languageCode: Option[String],
                canJoinGroups: Option[Boolean],
                canReadAllGroupMessages: Option[Boolean],
                supportsInlineQueries: Option[Boolean],
                status: Option[UserStatus])

object User {
  def changeStatus(user: User, newStatus: UserStatus)(
    implicit clock: Clock
  ): (UserStatusChange, User)=
    (UserStatusChange(user, newStatus, LocalDateTime.now(clock)), user.copy(status = Some(newStatus)))
}
