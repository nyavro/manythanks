package com.nyavro.manythanks.ws

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.nyavro.manythanks.user.User
import com.nyavro.manythanks.ws.auth.Token
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

  //   /user/register
  val routes = {
    pathPrefix("v1") {
      pathPrefix("user") {
        path("register") {
          pathEndOrSingleSlash {
            post {
              entity(as[User]) { user =>
                val map: Future[JsValue] = Future(User(Some(123L), "asdf", "login", "pwd")).map(_.toJson)
                complete(Created -> map)
              }
            }
          }
        }
      }
    }
  }

  Http().bindAndHandle(routes, httpInterface, httpPort)
}
