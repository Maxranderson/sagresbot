package sagresbot.entrypoints

import java.time.Clock
import sagresbot.core.usecases.{UserStatusUseCases, UserUseCases}
import sagresbot.core.{UserRepository, UserStatusChangeRepository, UserStatusRepository}

class CommandsEntryPoints(
  userRepository: UserRepository,
  userStatusChangeRepository: UserStatusChangeRepository,
  userStatusRepository: UserStatusRepository,
  clock: Clock
) {

  private def changeStatusCommand(
    statusName: String, description: String
  ): Command =
    Command(statusName, description, {
      case UpdateRequest(command, user) =>
        UserStatusUseCases.findByName(statusName)(userStatusRepository).flatMap {
          case Some(status) =>
            UserUseCases.changeStatus(user, status)(clock, userRepository, userStatusChangeRepository)
            UserUseCases.listAllWithLastStatus()(userRepository)
              .map(UserResources.usersStatusReponse)
          case None =>
            println(s"Falha: Status $statusName não encontrado")
            UserUseCases.listAllWithLastStatus()(userRepository)
              .map(UserResources.usersStatusReponse)
        }
    })

  private val indisponivelCommand: Command =
    changeStatusCommand("indisponivel", "Modifica para o status que indica offline")

  private val disponivelCommand: Command =
    changeStatusCommand("disponivel", "Modifica para o status que indica online")

  private val getStatusCommand: Command =
    Command("status", "Obtém o status atual de todos os usuários",
      _ =>
        UserUseCases.listAllWithLastStatus()(userRepository)
          .map {
            case u if u.isEmpty => UpdateResponse("Sem usuários no momento.")
            case u => UserResources.usersStatusReponse(u)
          }
    )

  val commands: Commands =
    Commands.define(indisponivelCommand, disponivelCommand, getStatusCommand)

}
