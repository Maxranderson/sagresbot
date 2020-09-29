package sagresbot.core.usecases

import java.time.Clock

import sagresbot.core.{UserRepository, UserStatusChangeRepository}
import sagresbot.core.entity.{User, UserStatus}

import scala.util.{Success, Try}

object UserUseCases {

  def changeStatus(user: User, newStatus: UserStatus): (Clock, UserRepository, UserStatusChangeRepository) => Try[User] =
    (clock, userRepo, userStatusRepo) =>
      user match {
        case u: User if !u.status.contains(newStatus) =>
          val (statusChange, userChanged) = User.changeStatus(u, newStatus)(clock)
          for {
            userUpdated <- userRepo.update(userChanged)
            _ <- userStatusRepo.create(statusChange)
          } yield userUpdated
        case _ => Success(user)
      }
  def getOrCreateUser(user: User): UserRepository => Try[User] =
    userRepository =>
      for {
        maybeUser <- findByFirstName(user.firstName)(userRepository)
        user <- maybeUser.map(Success(_)).getOrElse(
          createUser(user)(userRepository)
        )
      } yield user

  def createUser(user: User): UserRepository => Try[User] =
    _.create(user)

  def findByFirstName(firstName: String): UserRepository => Try[Option[User]] =
    _.findByFirstName(firstName)

  def listAll(): UserRepository => Try[Seq[User]] =
    _.listAll()

}
