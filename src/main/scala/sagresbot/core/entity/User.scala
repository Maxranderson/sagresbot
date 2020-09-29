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
                status: Option[UserStatus]) {
  override def equals(obj: Any): Boolean =
    obj match {
      case that: User =>
        this.id == that.id &&
        this.isBot == that.isBot &&
        this.firstName == that.firstName &&
        this.lastName == that.lastName &&
        this.username == that.username
      case _ => false
    }

  override def hashCode(): Int =
    Seq(id,isBot,firstName,lastName,username)
      .map(_.hashCode())
      .foldLeft(0)((a, b) => 31 * a + b)
}

object User {
  def changeStatus(user: User, newStatus: UserStatus)(
    implicit clock: Clock
  ): (UserStatusChange, User)=
    (UserStatusChange(user, newStatus, LocalDateTime.now(clock)), user.copy(status = Some(newStatus)))
}
