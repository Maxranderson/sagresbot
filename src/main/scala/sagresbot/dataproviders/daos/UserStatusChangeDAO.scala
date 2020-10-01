package sagresbot.dataproviders.daos

import java.time.LocalDateTime

case class UserStatusChangeDAO(
  userId: Long,
  statusName: String,
  updatedAt: LocalDateTime
)
