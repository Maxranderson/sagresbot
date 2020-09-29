package sagresbot.parsers

import com.pengrad.telegrambot.model.BotCommand
import sagresbot.entrypoints.Commands

object CommandParsers {

  def toBotCommand(commands: Commands): Seq[BotCommand] =
    commands.value.map(c => new BotCommand(c.name, c.description))

}
