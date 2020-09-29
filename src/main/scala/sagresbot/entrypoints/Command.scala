package sagresbot.entrypoints

import scala.util.Try

case class Command(name: String, description: String, apply: UpdateRequest => Try[UpdateResponse])