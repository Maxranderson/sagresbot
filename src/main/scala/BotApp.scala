
import com.pengrad.telegrambot.request.{GetMe, SendMessage, SetMyCommands}
import com.pengrad.telegrambot.{TelegramBot, UpdatesListener}
import sagresbot.configuration.Binders._
import sagresbot.configuration.Configuration
import sagresbot.core.usecases.UserUseCases._
import sagresbot.core.usecases.UserStatusUseCases
import sagresbot.entrypoints.{UpdateRequest, UpdateResponse}
import sagresbot.helpers.BotHelpers._
import sagresbot.parsers.CommandParsers
import sagresbot.parsers.UserParsers._
import scala.collection.JavaConverters._
import scala.util.Try

object BotApp extends App {

  println("Iniciando bot...")

  UserStatusUseCases.init()(userStatusRepository)

  var config = Configuration.fromEnv().get

  val bot = new TelegramBot(config.botToken)

  val botUser = bot.execute(new GetMe()).user()

  println(s"Bot ${botUser.username()} encontrado")

  bot.execute(new SetMyCommands(CommandParsers.toBotCommand(commandsEntryPoints.commands):_*))

  println(s"Comandos atualizados")

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
        response <- commandsEntryPoints.commands(UpdateRequest(command, user))
        responseSent <- Try(bot.execute(new SendMessage(chat.id(), response.message)))
      } yield {
        println(s"Response: ${responseSent.message().text()}")
        if(response != UpdateResponse.NotFoundResponse) {
          buildDeleteMessage(config).foreach(d => bot.execute(d))
          config = config.updateLastStatusMessageSendId(responseSent.message())
          buildPinMessage(config).foreach(p => bot.execute(p))
        }
      }
    }
    UpdatesListener.CONFIRMED_UPDATES_ALL
  })

}
