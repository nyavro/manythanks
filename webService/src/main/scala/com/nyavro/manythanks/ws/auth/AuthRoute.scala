package com.nyavro.manythanks.ws.auth

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.nyavro.manythanks.ws.route.RouteProvider
import com.nyavro.manythanks.ws.user.{User, UserProtocol}
import spray.json._

import scala.concurrent.ExecutionContext

class AuthRoute(authService:AuthService)(
  implicit val system:ActorSystem,
  implicit val executor:ExecutionContext,
  implicit val materializer:ActorMaterializer) extends RouteProvider with SprayJsonSupport with UserProtocol with AuthProtocol {

  private case class LoginRequest(login: String, password: String)
  private case class UserToken(userId:Long, token:String)

  private implicit val loginPasswordFormat = jsonFormat2(LoginRequest)
  private implicit val userTokenFormat = jsonFormat2(UserToken)

  override def route: Route = {
    pathPrefix("auth") {
      path("signUp") {
        pathEndOrSingleSlash {
          post {
            entity(as[User]) { user =>
              complete(Created -> authService.signUp(user).map(toUserToken).map(_.toJson))
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

  private def toUserToken(op:Option[Token]):Option[UserToken] =
    for {
      token <- op;
      userId <- token.userId
    } yield UserToken(userId, token.token)
}
