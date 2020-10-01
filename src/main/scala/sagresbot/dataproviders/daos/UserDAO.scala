package sagresbot.dataproviders.daos

import sagresbot.core.entity.{User, UserStatus}

case class UserDAO(id: Long,
                isBot: Boolean,
                firstName: String,
                lastName: Option[String],
                username: Option[String],
                languageCode: Option[String],
                canJoinGroups: Option[Boolean],
                canReadAllGroupMessages: Option[Boolean],
                supportsInlineQueries: Option[Boolean])

object UserDAO {
  def toUser(userDAO: UserDAO, status: Option[UserStatus]): User =
    User(
      userDAO.id, userDAO.isBot, userDAO.firstName, userDAO.lastName, userDAO.username, userDAO.languageCode,
      userDAO.canJoinGroups, userDAO.canReadAllGroupMessages, userDAO.supportsInlineQueries, status
    )

  def fromUser(user: User): UserDAO =
    UserDAO(user.id, user.isBot, user.firstName, user.lastName, user.username,
      user.languageCode, user.canJoinGroups, user.canReadAllGroupMessages, user.supportsInlineQueries)
}
