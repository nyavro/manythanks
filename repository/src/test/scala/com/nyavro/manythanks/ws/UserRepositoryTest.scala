package com.nyavro.manythanks.ws

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global

class UserRepositoryTest extends WordSpec with Matchers with ScalaFutures with Config {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  "User repository" ignore {
    "create user" in {
      whenReady(
        for (
          created <- UserRepository.create(UserEntity(None, "user", "1POzF21mPfK", "123-321"));
          remove <- UserRepository.delete(created.id.get)
        ) yield created
      ) { result =>
        result.id.isDefined should === (true)
      }
    }
    "load user by id" in {
      whenReady(
        for (
          created <- UserRepository.create(UserEntity(None, "user3", "F21mPfK1POz", "4123-32"));
          loaded <- UserRepository.load(created.id.get);
          remove <- UserRepository.delete(created.id.get)
        ) yield loaded
      ) { result =>
        result.isDefined should === (true)
        result.get.login should === ("user3")
        result.get.gcmToken should === ("F21mPfK1POz")
        result.get.extId should === ("4123-32")
      }
    }
  }
}
