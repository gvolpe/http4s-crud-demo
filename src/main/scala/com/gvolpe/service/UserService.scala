package com.gvolpe.service

import com.gvolpe.http.model._
import com.gvolpe.service.UserService.UserSorting

import scalaz.concurrent.Task

object UserService extends UserService {

  private var usernames = scala.collection.mutable.Set[String]("gvolpe", "modersky")

  private var users = scala.collection.mutable.Map[Long, User](
    1L -> User(1L, "gvolpe", "gvolpe@github.com", 29),
    2L -> User(2L, "modersky", "modersky@github.com", 58)
  )

  sealed trait UserSorting extends Product with Serializable
  case object Asc extends UserSorting
  case object Desc extends UserSorting

  object UserSorting {
    def from(sort: Option[String]) = sort match {
      case Some(v) if v.equalsIgnoreCase("desc")  => Desc
      case _                                      => Asc
    }
  }

  case class UserNotFoundException(id: Long) extends Exception
  case class DuplicatedUsernameException(username: String) extends Exception

  private def add(user: User): Task[Unit] = Task.delay {
    usernames += user.username
    users += (user.id -> user)
  }

  override def save(user: User): Task[Unit] = {
    if (usernames.contains(user.username)) Task.fail(DuplicatedUsernameException(user.username))
    else add(user)
  }

  override def find(id: Long): Task[User] = users.get(id) match {
    case Some(user) => Task.now(user)
    case None       => Task.fail(UserNotFoundException(id))
  }

  private def findAllUnsorted = Task.delay { users.values.toList }

  override def findAll(sort: UserSorting): Task[List[User]] = sort match {
    case Asc  => findAllUnsorted.map(_.sortBy(_.username))
    case Desc => findAllUnsorted.map(_.sortBy(_.username).reverse)
  }

  override def remove(id: Long): Task[Unit] = users.get(id) match {
    case Some(user) => Task.delay {
      usernames = usernames -= user.username
      users = users -= id
    }
    case None       => Task.fail(UserNotFoundException(id))
  }

  override def edit(id: Long, age: Int): Task[Unit] = users.get(id) match {
    case Some(user) => Task.delay { users.update(id, user.copy(age = age)) }
    case None       => Task.fail(UserNotFoundException(id))
  }

}

trait UserService {
  def save(user: User): Task[Unit]
  def findAll(sort: UserSorting): Task[List[User]]
  def find(id: Long): Task[User]
  def remove(id: Long): Task[Unit]
  def edit(id: Long, age: Int): Task[Unit]
}
