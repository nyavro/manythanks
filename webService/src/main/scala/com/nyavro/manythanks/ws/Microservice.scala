package com.nyavro.manythanks.ws

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.nyavro.manythanks.user.User
import com.nyavro.manythanks.ws.auth.{AuthService, AuthRoute, Token}
import spray.json.{DefaultJsonProtocol, _}

import scala.concurrent.Future

trait Protocols extends DefaultJsonProtocol {
  implicit val usersFormat = jsonFormat4(User)
  implicit val tokensFormat = jsonFormat3(Token)
}

object Microservice extends App with Config with Protocols with SprayJsonSupport {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val logger = Logging(system, getClass)

  val authRoute = new AuthRoute(
    new AuthService {
      override def signUp(user: User): Future[Option[Token]] = Future(Some(Token(Some(3L), Some(4L), "signUpSucceessfullToken")))
      override def signIn(login: String, password: String) = Future(Some(Token(Some(5L), Some(6L), "signInSucceessfullToken")))
    }
  )

  val routes = {
    pathPrefix("v1") {
      authRoute.route
    }
  }

  Http().bindAndHandle(routes, httpInterface, httpPort)
}
