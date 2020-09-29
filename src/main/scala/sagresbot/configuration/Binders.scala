package sagresbot.configuration

import java.time.Clock

import sagresbot.core.{UserRepository, UserStatusChangeRepository, UserStatusRepository}
import sagresbot.dataproviders.{UserInMemoryRepository, UserStatusChangeInMemoryRepository, UserStatusInMemoryRepository}
import sagresbot.entrypoints.CommandsEntryPoints

object Binders {
  val userRepository: UserRepository = new UserInMemoryRepository()
  val userStatusRepository: UserStatusRepository = new UserStatusInMemoryRepository()
  val userStatusChangeInMemoryRepository: UserStatusChangeRepository = new UserStatusChangeInMemoryRepository()
  val commandsEntryPoints: CommandsEntryPoints =
    new CommandsEntryPoints(userRepository, userStatusChangeInMemoryRepository,
      userStatusRepository, Clock.systemDefaultZone())
}
