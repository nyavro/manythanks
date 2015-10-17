package com.nyavro.manythanks.ws.auth

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.nyavro.manythanks.ws.route.RouteProvider
import com.nyavro.manythanks.ws.user.{User, UserFormat}
import spray.json._

import scala.concurrent.ExecutionContextExecutor

class AuthRoute(authService:AuthService)
  (implicit private val system:ActorSystem,
   implicit private val executor: ExecutionContextExecutor,
   implicit private val materializer: ActorMaterializer) extends RouteProvider
      with SprayJsonSupport with UserFormat with TokenFormat {
  private case class LoginRequest(login: String, password: String)

  private implicit val loginPasswordFormat = jsonFormat2(LoginRequest)

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
      } ~
      path("signIn") {
        pathEndOrSingleSlash {
          post {
            entity(as[LoginRequest]) { loginRequest =>
              complete(authService.signIn(loginRequest.login, loginRequest.password).map(_.toJson))
            }
          }
        }
      }
    }
  }
}
