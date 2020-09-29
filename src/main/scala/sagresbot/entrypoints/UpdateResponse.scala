package sagresbot.entrypoints

import scala.util.{Success, Try}

case class UpdateResponse(message: String)

object UpdateResponse {
  val NotFoundResponse = UpdateResponse("Não faço ideia do que você quer.")

  val NotFoundCommand = Command("", "", _ => Success(NotFoundResponse))
}
