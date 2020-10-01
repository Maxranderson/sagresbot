package sagresbot.core.usecases

import sagresbot.core.UserStatusRepository
import sagresbot.core.entity.UserStatus

import scala.util.{Success, Try}

object UserStatusUseCases {

  def init(): UserStatusRepository => Try[Seq[UserStatus]] =
    repo =>
      for {
        existentes <- this.listAll()(repo)
        all <- Seq(UserStatus.INDISPONIVEL, UserStatus.DISPONIVEL)
          .map(s => existentes.find(_.name == s.name).map(Success(_)).getOrElse(repo.create(s)))
          .foldLeft(Try(Seq.empty[UserStatus]))((t1, t2) => t1.flatMap(u1 => t2.map(u2 => u1 :+ u2 )))
      } yield all


  def listAll(): UserStatusRepository => Try[Seq[UserStatus]] =
    _.listAll()

  def findByName(name: String): UserStatusRepository => Try[Option[UserStatus]] =
    _.findByName(name)

}
