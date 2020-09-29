package sagresbot.configuration

import com.pengrad.telegrambot.model.{Chat, Message}
import scala.util.{Failure, Success, Try}

case class Configuration(
  botToken: String, groupTitleToPinMessage: String,
  groupIdToPinMessage: Option[Long] = None, lastStatusMessageSendId: Option[Int] = None
) {
  def isGroupTitleToPinMessage(chat: Chat): Boolean =
    Option(chat.title()).exists(_.contains(groupTitleToPinMessage))

  def updateGroupIdToPinMessage(chat: Chat): Configuration =
    this.copy(groupIdToPinMessage = Some(chat.id()))

  def updateLastStatusMessageSendId(message: Message): Configuration =
    this.copy(lastStatusMessageSendId = Some(message.messageId()))
}

object Configuration {

  private val BOT_TOKEN_ENV = "BOT_TOKEN"
  private val GROUD_TITLE_TO_PIN_MESSAGE_ENV = "GROUD_TITLE_TO_PIN_MESSAGE"

  private def getEnv(envName: String): Try[String] =
    Option(System.getenv(envName))
      .filter(_.nonEmpty)
      .map(Success(_))
      .getOrElse(Failure(new Exception(s"Variável de ambiente $envName não encontrada")))

  def fromEnv(): Try[Configuration] =
    for {
      botToken <- getEnv(BOT_TOKEN_ENV)
      groupTitleToPinMessage <- getEnv(GROUD_TITLE_TO_PIN_MESSAGE_ENV)
    } yield Configuration(botToken, groupTitleToPinMessage)

}
