import java.util.regex.Pattern

import com.pengrad.telegrambot.model.{Chat, Message, Update, User => TelegramUser}
import com.pengrad.telegrambot.request.{DeleteMessage, GetMe, PinChatMessage, SendMessage}
import com.pengrad.telegrambot.{TelegramBot, UpdatesListener}
import sagresbot.core.usecases.{UserStatusUseCases, UserUseCases}
import sagresbot.entrypoints.{CommandsEntryPoints, UpdateRequest, UpdateResponse}
import sagresbot.parsers.UserParsers._
import collection.JavaConverters._
import scala.util.{Failure, Success, Try}
import sagresbot.configuration.Binders._
import sagresbot.configuration.Configuration
import sagresbot.core.entity.User
import sagresbot.helpers.BotHelpers._
import UserUseCases._

object BotApp extends App {

  println("Iniciando bot...")

  UserStatusUseCases.init()(userStatusRepository)

  var config = Configuration.fromEnv().get

  val bot = new TelegramBot(config.botToken)

  val botUser = bot.execute(new GetMe()).user()

  println(s"Bot ${botUser.username()} encontrado")

  bot.setUpdatesListener(updates => {
    println("Processando novos eventos")
    updates.asScala.filter(onlyCommands(botUser)).foreach { update =>
      val message = update.message()
      val chat = message.chat()
      val messageText = Option(message.text()).getOrElse("")
      val userFirstName = message.from().firstName()
      println(s"@$userFirstName: $messageText")
      if(config.isGroupTitleToPinMessage(chat)) config = config.updateGroupIdToPinMessage(chat)
      for {
        user <- getOrCreateUser(fromTelegramUser(message.from()))(userRepository)
        command <- getCommand(message, botUser)
        response <- commandsEntryPoints.commands orElse UpdateResponse.notFound apply UpdateRequest(command, user)
        responseSent <- Try(bot.execute(new SendMessage(chat.id(), response.message)))
      } yield {
        println(s"Response: ${message.text()}")
        if(response != UpdateResponse.CommandNotFound) {
          buildDeleteMessage(config).foreach(d => bot.execute(d))
          config = config.updateLastStatusMessageSendId(responseSent.message())
          buildPinMessage(config).foreach(p => bot.execute(p))
        }
      }
    }
    UpdatesListener.CONFIRMED_UPDATES_ALL
  })

}
