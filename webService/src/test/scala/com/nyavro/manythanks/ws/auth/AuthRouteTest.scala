package com.nyavro.manythanks.ws.auth

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.nyavro.manythanks.ws.user.User
import com.nyavro.manythanks.ws.Protocols
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}
import spray.json.{JsValue, _}

import scala.concurrent.Future

class AuthRouteTest extends WordSpec with Matchers with ScalatestRouteTest with Protocols with MockFactory {
  "Authentication service signUp" should {

    "Create user and return user's token" in {
      val newUser = User(Some(3L), "26 10 30", "Mars", "test1")
      val mockToken = Token(Some(123L), Some(1L), "123token321")
      val authService = stub[AuthService]
      authService.signUp _ when newUser returning Future(Some(mockToken))
      val authRoute = new AuthRoute(
        authService
      )
      Post(
        "/auth/signUp",
        HttpEntity(MediaTypes.`application/json`, newUser.toJson.toString())
      ) ~> authRoute.route ~> check {
        response.status should be(StatusCodes.Created)
        mockToken should be(tokensFormat.read(responseAs[JsValue]))
      }
    }
  }

  "SignIn" should {
    val mockToken = Token(Some(234L), Some(2L), "555tkntkn731")
    val login = "test"
    val password = "pwd"
    val authRoute = new AuthRoute(
      new AuthService {
        override def authenticate(token: String): Future[Option[User]] = ???
        override def signUp(user: User): Future[Option[Token]] = ???
        override def signIn(lgn:String, pwd:String) = Future(
          if(lgn==login && pwd==password) Some(mockToken)
          else None
        )
      }
    )
    "Retrieve token of existing user" in {
      Post(
        "/auth/signIn",
        HttpEntity(MediaTypes.`application/json`, "{\"login\":\"%s\",\"password\":\"%s\"}".format(login, password))
      ) ~> authRoute.route ~> check {
        response.status should be(StatusCodes.OK)
        mockToken should be (tokensFormat.read(responseAs[JsValue]))
      }
    }
  }
}
