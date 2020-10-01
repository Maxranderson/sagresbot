package sagresbot.core

import sagresbot.core.entity.User

import scala.util.Try

trait UserRepository {
  def listAll(): Try[Seq[User]]
  def listAllWithLastStatus(): Try[Seq[User]]
  def findByFirstNameWithLastStatus(firstName: String): Try[Option[User]]
  def create(user: User): Try[User]
  def update(user: User): Try[User]
}
