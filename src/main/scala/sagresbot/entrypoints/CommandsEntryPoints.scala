package sagresbot.entrypoints

import java.time.Clock

import sagresbot.core.usecases.{UserStatusUseCases, UserUseCases}
import sagresbot.core.{UserRepository, UserStatusChangeRepository, UserStatusRepository}
import UpdateResponse.CommandNotFound

import scala.util.{Success, Try}

class CommandsEntryPoints(userRepository: UserRepository, userStatusChangeRepository: UserStatusChangeRepository, userStatusRepository: UserStatusRepository, clock: Clock) {

  private def changeStatusPartial(statusName: String): PartialFunction[UpdateRequest, Try[UpdateResponse]] = {
    case UpdateRequest(command, user) if command == statusName =>
      UserStatusUseCases.findByName(statusName)(userStatusRepository).flatMap {
        case Some(status) =>
          UserUseCases.changeStatus(user, status)(clock, userRepository, userStatusChangeRepository)
          UserUseCases.listAll()(userRepository)
            .map(UserResources.usersStatusReponse)
        case None =>
          println(s"Falha: Status $statusName não encontrado")
          UserUseCases.listAll()(userRepository)
            .map(UserResources.usersStatusReponse)
      }
  }

  private val indisponivelCommand: PartialFunction[UpdateRequest, Try[UpdateResponse]] =
    changeStatusPartial("indisponivel")

  private val disponivelCommand: PartialFunction[UpdateRequest, Try[UpdateResponse]] =
    changeStatusPartial("disponivel")

  private val getStatusCommand: PartialFunction[UpdateRequest, Try[UpdateResponse]] = {
    case UpdateRequest("status", _) =>
      UserUseCases.listAll()(userRepository)
        .map {
          case u if u.isEmpty => UpdateResponse("Sem usuários no momento.")
          case u => UserResources.usersStatusReponse(u)
        }
  }

  val commands: PartialFunction[UpdateRequest, Try[UpdateResponse]] =
    indisponivelCommand orElse disponivelCommand orElse getStatusCommand

}
