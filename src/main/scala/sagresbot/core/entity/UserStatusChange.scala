package sagresbot.core.entity

import java.time.LocalDateTime

case class UserStatusChange(user: User, status: UserStatus, updatedAt: LocalDateTime)
