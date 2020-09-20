import java.util.regex.Pattern

import com.pengrad.telegrambot.model.{Chat, Message, Update, User => TelegramUser}
import com.pengrad.telegrambot.request.{DeleteMessage, GetMe, PinChatMessage, SendMessage}
import com.pengrad.telegrambot.{TelegramBot, UpdatesListener}
import sagresbot.core.usecases.{UserStatusUseCases, UserUseCases}
import sagresbot.entrypoints.{CommandsEntryPoints, UpdateRequest, UpdateResponse}
import sagresbot.parsers.UserParsers

import collection.JavaConverters._
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex
import sagresbot.configuration.Binders._
import sagresbot.configuration.Configuration
import sagresbot.core.entity.User

object BotApp extends App {

  println("Iniciando bot...")

  UserStatusUseCases.init()(userStatusRepository)

  var config = Configuration.fromEnv().get

  val bot = new TelegramBot(config.botToken)

  val botUser = bot.execute(new GetMe()).user()

  println(s"Bot ${botUser.username()} encontrado")

  def onlyCommands(update: Update): Boolean =
    Option(update.message())
      .flatMap(m => Option(m.text()))
      .filter(_.nonEmpty)
      .exists(_.endsWith(s"@${botUser.username()}"))

  def ifIsGroupTitleToPinMessageSetGroupIdToPinMessage(chat: Chat): Unit =
    if(config.isGroupTitleToPinMessage(chat)) config = config.updateGroupIdToPinMessage(chat)

  def createOrUpdateUser(message: Message): Try[User] =
    for {
      maybeUser <- UserUseCases.findByFirstName(message.from().firstName())(userRepository)
      user <- maybeUser.map(Success(_)).getOrElse(
        UserUseCases.createUser(UserParsers.fromTelegramUser(message.from()))(userRepository)
      )
    } yield user

  def deleteLastStatusMessage(): Unit =
    for {
      groupIdToPinMessage <- config.groupIdToPinMessage
      lastStatusMessageSendId <- config.lastStatusMessageSendId
    } bot.execute(new DeleteMessage(groupIdToPinMessage, lastStatusMessageSendId))

  def pinLastStatusMessage(): Unit =
    for {
      groupIdToPinMessage <- config.groupIdToPinMessage
      lastStatusMessageSendId <- config.lastStatusMessageSendId
    } bot.execute(
      new PinChatMessage(groupIdToPinMessage, lastStatusMessageSendId)
        .disableNotification(true)
    )

  def getCommand(message: Message): Try[String] =
    new Regex("^\\/(\\w+)@?("+ botUser.username() + ")?$")
      .findFirstMatchIn(message.text())
      .map(_.group(1))
      .map(Success(_))
      .getOrElse(Failure(new Exception("Falha ao obter o comando.")))

  bot.setUpdatesListener(updates => {
    println("Processando novos eventos")
    updates.asScala.filter(onlyCommands).foreach { update =>
      val message = update.message()
      val chat = message.chat()
      val messageText = Option(message.text()).getOrElse("")
      val userFirstName = message.from().firstName()
      println(s"@$userFirstName: $messageText")
      ifIsGroupTitleToPinMessageSetGroupIdToPinMessage(chat)
      for {
        user <- createOrUpdateUser(message)
        command <- getCommand(message)
        response <- commandsEntryPoints.commands orElse UpdateResponse.notFound apply UpdateRequest(command, user)
        responseSent <- Try(bot.execute(new SendMessage(chat.id(), response.message)))
      } yield {
        println(s"Response: ${message.text()}")
        if(response != UpdateResponse.CommandNotFound) {
          deleteLastStatusMessage()
          config = config.updateLastStatusMessageSendId(responseSent.message())
          pinLastStatusMessage()
        }
      }
    }
    UpdatesListener.CONFIRMED_UPDATES_ALL
  })

}
