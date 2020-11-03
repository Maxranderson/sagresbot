package sagresbot.configuration

import java.time.Clock

import sagresbot.core.{UserRepository, UserStatusChangeRepository, UserStatusRepository}
import sagresbot.dataproviders.{UserInMemoryRepository, UserSqlRepository, UserStatusChangeInMemoryRepository, UserStatusChangeSqlRepository, UserStatusInMemoryRepository, UserStatusSqlRepository}
import sagresbot.entrypoints.CommandsEntryPoints

object Binders {
  val userRepository: UserRepository = new UserSqlRepository()
  val userStatusRepository: UserStatusRepository = new UserStatusSqlRepository()
  val userStatusChangeInMemoryRepository: UserStatusChangeRepository = new UserStatusChangeSqlRepository()
  val commandsEntryPoints: CommandsEntryPoints =
    new CommandsEntryPoints(userRepository, userStatusChangeInMemoryRepository,
      userStatusRepository, Clock.systemDefaultZone())
}
