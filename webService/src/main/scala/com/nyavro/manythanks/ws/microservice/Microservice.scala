package com.nyavro.manythanks.ws.microservice

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.nyavro.manythanks.ws.auth.{AuthRoute, AuthService, Token}
import com.nyavro.manythanks.ws.contact.{Contact, ContactRoute, ContactService}
import com.nyavro.manythanks.ws.mark.{Mark, MarkRoute, MarkService}
import com.nyavro.manythanks.ws.security.Directives
import com.nyavro.manythanks.ws.user.User

import scala.concurrent.Future

object Microservice extends App with Config with SprayJsonSupport {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  val logger = Logging(system, getClass)
  val authService = new AuthService {
    override def authenticate(token: String): Future[Option[User]] = Future(
      token match {
        case "signInSucceessfullToken" => Some(User(Some(31L), "12331", "login", "passwd123"))
        case _ => None
      }
    )
    override def signUp(user: User): Future[Option[Token]] = Future(Some(Token(Some(3L), Some(4L), "signUpSucceessfullToken")))
    override def signIn(login: String, password: String) = Future(
      if(login=="test") Some(Token(Some(5L), Some(6L), "signInSucceessfullToken"))
      else None
    )
  }
  val directives = new Directives(authService)
  val authRoute = new AuthRoute(
    authService
  )
  val markRoute = new MarkRoute(
    new MarkService {
      override def create(mark: Mark, from: Long) = Future(Some(mark))
    },
    directives
  )
  val contactRoute = new ContactRoute(
    new ContactService {
      override def list(extIds: List[String]): Future[List[Contact]] = Future(extIds.map(extId => Contact(System.currentTimeMillis(), extId)))
    },
    directives
  )
  val routes = {
    pathPrefix("v1") {
      authRoute.route ~
      markRoute.route ~
      contactRoute.route
    }
  }

  Http().bindAndHandle(routes, httpInterface, httpPort)
}
