package sagresbot.entrypoints

import scala.util.{Success, Try}

case class UpdateResponse(message: String)

object UpdateResponse {
  val CommandNotFound = UpdateResponse("Não faço ideia do que você quer.")

  val notFound : PartialFunction[UpdateRequest, Try[UpdateResponse]] = {
    case _ => Success(CommandNotFound)
  }
}
