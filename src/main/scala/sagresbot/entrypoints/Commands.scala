package sagresbot.entrypoints

import scala.util.{Success, Try}

case class Commands(value: Seq[Command]){

  def add(command: Command): Commands =
    this.copy(value :+ command)

  def apply(updateRequest: UpdateRequest): Try[UpdateResponse] =
    value.find(_.name == updateRequest.command)
      .map(_.apply(updateRequest))
      .getOrElse(Success(UpdateResponse.NotFoundResponse))
}

object Commands {
  def define(commands: Command*): Commands = new Commands(commands)
}
