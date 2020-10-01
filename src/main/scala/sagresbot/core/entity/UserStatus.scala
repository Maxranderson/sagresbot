package sagresbot.core.entity

import io.getquill.Embedded

case class UserStatus(name: String, description: String, icon: Option[String]) extends Embedded

object UserStatus {
  val DISPONIVEL = UserStatus("disponivel", "Disponível", Some("\uD83D\uDFE2"))
  val INDISPONIVEL = UserStatus("indisponivel", "Indisponível", Some("⚫️"))
}