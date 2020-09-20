package sagresbot.parsers

import com.pengrad.telegrambot.model.{User => TelegramUser}
import sagresbot.core.entity.User

object UserParsers {

  def fromTelegramUser(user: TelegramUser): User =
    User(
      user.id().toLong,
      user.isBot,
      user.firstName(),
      Option(user.lastName()),
      Option(user.username()),
      Option(user.languageCode()),
      Option(user.canJoinGroups),
      Option(user.canReadAllGroupMessages),
      Option(user.supportsInlineQueries()),
      None
    )

}
