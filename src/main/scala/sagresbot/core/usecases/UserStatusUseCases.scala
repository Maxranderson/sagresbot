package sagresbot.core.usecases

import sagresbot.core.UserStatusRepository
import sagresbot.core.entity.UserStatus

import scala.util.Try

object UserStatusUseCases {

  def init(): UserStatusRepository => Try[Seq[UserStatus]] =
    repo =>
      Seq(UserStatus.INDISPONIVEL, UserStatus.DISPONIVEL)
        .map(repo.create)
        .foldLeft(Try(Seq.empty[UserStatus]))((t1, t2) => t1.flatMap(u1 => t2.map(u2 => u1 :+ u2 )))


  def listAll(): UserStatusRepository => Try[Seq[UserStatus]] =
    _.listAll()

  def findByName(name: String): UserStatusRepository => Try[Option[UserStatus]] =
    _.findByName(name)

}
