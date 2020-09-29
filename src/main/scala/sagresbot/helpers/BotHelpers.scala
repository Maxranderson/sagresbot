package sagresbot.helpers

import com.pengrad.telegrambot.model.{Message, Update, User}
import com.pengrad.telegrambot.request.{DeleteMessage, PinChatMessage}
import sagresbot.configuration.Configuration

import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

object BotHelpers {

  private def commandRegex(botUser: User) = new Regex("^\\/(\\w+)@?(" + botUser.username() + ")?$")

  def getCommand(message: Message, botUser: User): Try[String] =
    commandRegex(botUser)
      .findFirstMatchIn(message.text())
      .map(_.group(1))
      .map(Success(_))
      .getOrElse(Failure(new Exception("Falha ao obter o comando.")))

  def buildPinMessage(config: Configuration): Option[PinChatMessage] =
    for {
      groupIdToPinMessage <- config.groupIdToPinMessage
      lastStatusMessageSendId <- config.lastStatusMessageSendId
    } yield
      new PinChatMessage(groupIdToPinMessage, lastStatusMessageSendId)
        .disableNotification(true)

  def buildDeleteMessage(config: Configuration): Option[DeleteMessage] =
    for {
      groupIdToPinMessage <- config.groupIdToPinMessage
      lastStatusMessageSendId <- config.lastStatusMessageSendId
    } yield new DeleteMessage(groupIdToPinMessage, lastStatusMessageSendId)

  def onlyCommands(botUser: User)(update: Update): Boolean =
    Option(update.message())
      .flatMap(m => Option(m.text()))
      .filter(_.nonEmpty)
      .flatMap(commandRegex(botUser).findFirstMatchIn)
      .isDefined

}
