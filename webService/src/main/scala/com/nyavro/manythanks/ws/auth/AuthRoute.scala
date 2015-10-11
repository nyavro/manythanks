package com.nyavro.manythanks.ws.auth

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.nyavro.manythanks.user.User
import com.nyavro.manythanks.ws.Protocols
import spray.json._

class AuthRoute(authService:AuthService) extends RouteProvider with SprayJsonSupport with Protocols {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  override def route: Route = {
    pathPrefix("auth") {
      path("signUp") {
        pathEndOrSingleSlash {
          post {
            entity(as[User]) { user =>
              complete(Created -> authService.signUp(user).map(_.toJson))
            }
          }
        }
      }
    }
  }
}
