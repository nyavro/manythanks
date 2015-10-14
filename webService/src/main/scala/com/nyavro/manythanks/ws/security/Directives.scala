package com.nyavro.manythanks.ws.security

import akka.http.scaladsl.server._
import com.nyavro.manythanks.ws.auth.AuthService
import com.nyavro.manythanks.ws.user.User

class Directives(authService:AuthService) {

  val TokenHeader = "Token"

  import akka.http.scaladsl.server.directives.BasicDirectives._
  import akka.http.scaladsl.server.directives.FutureDirectives._
  import akka.http.scaladsl.server.directives.HeaderDirectives._
  import akka.http.scaladsl.server.directives.RouteDirectives._

  def authenticate: Directive1[User] = {
    headerValueByName(TokenHeader).flatMap { token =>
      onSuccess(authService.authenticate(token)).flatMap {
        case Some(user) => provide(user)
        case None       => reject
      }
    }
  }
}
