package sagresbot.configuration

import java.time.Clock

import sagresbot.core.{UserRepository, UserStatusChangeRepository, UserStatusRepository}
import sagresbot.dataproviders.{UserInMemoryRepository, UserSqlLiteRepository, UserStatusChangeInMemoryRepository, UserStatusChangeSqlLiteRepository, UserStatusInMemoryRepository, UserStatusSqlLiteRepository}
import sagresbot.entrypoints.CommandsEntryPoints

object Binders {
  val userRepository: UserRepository = new UserSqlLiteRepository()
  val userStatusRepository: UserStatusRepository = new UserStatusSqlLiteRepository()
  val userStatusChangeInMemoryRepository: UserStatusChangeRepository = new UserStatusChangeSqlLiteRepository()
  val commandsEntryPoints: CommandsEntryPoints =
    new CommandsEntryPoints(userRepository, userStatusChangeInMemoryRepository,
      userStatusRepository, Clock.systemDefaultZone())
}
