package com.peknight.demo.cats.datatypes

import cats.Eval
import cats.data.ContT

object ContTApp extends App:
  case class User(id: Int, name: String, age: Int)
  sealed abstract class UserUpdateResult derives CanEqual
  case class Succeeded(updatedUserId: Int) extends UserUpdateResult
  case object Failed extends UserUpdateResult

  def updateUser(persistToDatabase: User => Eval[UserUpdateResult])(existingUser: User, newName: String, newAge: Int)
  : Eval[UserUpdateResult] =
    val trimmedName = newName.trim
    val cappedAge = newAge max 150
    val updatedUser = existingUser.copy(name = trimmedName, age = cappedAge)
    persistToDatabase(updatedUser)

  def updateUserCont(existingUser: User, newName: String, newAge: Int): ContT[Eval, UserUpdateResult, User] =
    ContT[Eval, UserUpdateResult, User] { next =>
      val trimmedName = newName.trim
      val cappedAge = newAge max 150
      val updatedUser = existingUser.copy(name = trimmedName, age = cappedAge)
      next(updatedUser)
    }

  val existingUser = User(100, "Alice", 42)
  val computation = updateUserCont(existingUser, "Bob", 200)
  val eval = computation.run { user =>
    Eval.later {
      println(s"Persisting updated user to the DB: $user")
      Succeeded(user.id)
    }
  }
  eval.value

  val anotherComputation = computation.map { user =>
    Map(
      "id" -> user.id.toString,
      "name" -> user.name,
      "age" -> user.age.toString
    )
  }
  val anotherEval = anotherComputation.run { userFields =>
    Eval.later {
      println(s"Persisting these fields to the DB: $userFields")
      Succeeded(userFields("id").toInt)
    }
  }
  anotherEval.value

  val updateUserModel: ContT[Eval, UserUpdateResult, User] =
    updateUserCont(existingUser, "Bob", 200).map { updatedUser =>
      println("Updated user model")
      updatedUser
    }

  val persistToDb: User => ContT[Eval, UserUpdateResult, UserUpdateResult] =
    user => ContT[Eval, UserUpdateResult, UserUpdateResult] { next =>
      println(s"Persisting updated user to the DB: $user")
      next(Succeeded(user.id))
    }

  val publishEvent: UserUpdateResult => ContT[Eval, UserUpdateResult, UserUpdateResult] =
    userUpdateResult => ContT[Eval, UserUpdateResult, UserUpdateResult] { next =>
      userUpdateResult match
        case Succeeded(userId) =>
          println(s"Publishing 'user updated' event for user ID $userId")
        case Failed =>
          println("Not publishing 'user updated' event because update failed")
      next(userUpdateResult)
    }

  val chainOfContinuations = updateUserModel flatMap persistToDb flatMap publishEvent
  val eval3 = chainOfContinuations.run { finalResult => Eval.later {
    println("Finished!")
    finalResult
  }}
  eval3.value
end ContTApp
