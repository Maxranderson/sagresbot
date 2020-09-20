package sagresbot.entrypoints

import sagresbot.core.entity.User

case class UpdateRequest(command: String, user: User)
