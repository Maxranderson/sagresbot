package sagresbot.entrypoints

import sagresbot.core.entity.{User, UserStatus}

object UserResources {

  def usersStatusReponse(users: Seq[User]): UpdateResponse =
    UpdateResponse(
      users.map {
        case User(_, _, firstName, _, _, _, _, _, _, Some(UserStatus(_, _, Some(icon)))) =>
          s"$firstName $icon"
        case User(_, _, firstName, _, _, _, _, _, _, Some(UserStatus(_, desc, None))) =>
          s"$firstName $desc"
        case u =>
          s"${u.firstName} sem status"
      }.mkString("\n")
    )

}
