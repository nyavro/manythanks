package com.nyavro.manythanks.ws.mark

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, _}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.nyavro.manythanks.ws.auth.AuthService
import com.nyavro.manythanks.ws.security.Directives
import com.nyavro.manythanks.ws.user.User
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}
import spray.json.{JsObject, _}

import scala.concurrent.Future

class MarkRouteTest extends WordSpec with Matchers with ScalatestRouteTest with MarkFormat with MockFactory {
  "Mark route" should {
    "Create mark on authorized request" in {
      val newMark = Mark(None, "26-10-30", 35L, 51L, true, "Good luck!")
      val markService = stub[MarkService]
      (markService.create _) when (newMark) returns(Future(Some(newMark.copy(id = Some(3L)))))
      val authService = stub[AuthService]
      (authService.authenticate _) when ("validTokenValue") returns(Future(Some(User(Some(1), "32", "en", "pwd"))))
      val markRoute = new MarkRoute(markService,  new Directives(authService))
      Post(
        "/mark/create",
        HttpEntity(MediaTypes.`application/json`, newMark.toJson.toString())
      ) ~> addHeader("Token", "validTokenValue") ~> markRoute.route ~> check {
        responseAs[JsObject] should be(newMark.copy(id = Some(3L)).toJson)
        response.status should be(StatusCodes.Created)
      }
    }
    "Reject mark creation request on invalid token" in {
      val markService = mock[MarkService]
      val authService = stub[AuthService]
      (authService.authenticate _) when (*) returns(Future(None))
      val markRoute = new MarkRoute(markService, new Directives(authService))
      Post(
        "/mark/create",
        HttpEntity(MediaTypes.`application/json`, "anyContent")
      ) ~> addHeader("Token", "invalidToken") ~> markRoute.route ~> check {
        handled shouldBe false
      }
      (markService.create _).expects(*) never()
    }
    "Reject mark creation request on missing token" in {
      val markService = mock[MarkService]
      val authService = mock[AuthService]
      val markRoute = new MarkRoute(markService, new Directives(authService))
      Post(
        "/mark/create",
        HttpEntity(MediaTypes.`application/json`, "{anyContent}")
      ) ~> markRoute.route ~> check {
        handled shouldBe false
      }
      (markService.create _).expects(*) never()
      (authService.authenticate _).expects(*) never()
    }
  }
}

