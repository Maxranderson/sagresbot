package sagresbot.core.entity

case class UserStatus(name: String, description: String, icon: Option[String])

object UserStatus {
  val DISPONIVEL = UserStatus("disponivel", "Disponível", Some("\uD83D\uDFE2"))
  val INDISPONIVEL = UserStatus("indisponivel", "Indisponível", Some("⚫️"))
}