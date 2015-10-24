package com.nyavro.manythanks.ws.contact

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

class ContactRouteTest extends WordSpec with Matchers with ScalatestRouteTest with ContactFormat with MockFactory {
  "Contact route" should {
    "List contacts on authorized request" in {
      val contactService = stub[ContactService]
      contactService.list _ when List("1", "2", "3") returns Future(List(Contact("test", Some("123"))))
      val authService = stub[AuthService]
      authService.authenticate _ when "validTokenValue" returns Future(Some(User(Some(1), "32", "en", "pwd")))
      val contactRoute = new ContactRoute(contactService, new Directives(authService))
      Post(
        "/contact/list",
        HttpEntity(MediaTypes.`application/json`, """{"extIds":["1","2","3"]}""")
      ) ~> addHeader("Token", "validTokenValue") ~> contactRoute.route ~> check {
        responseAs[JsObject].toString should be(s"""{"list":${List(Contact("test", Some("123"))).toJson}}""")
        response.status should be(StatusCodes.OK)
      }
    }
    "Reject mark creation request on invalid token" in {
      val contactService = mock[ContactService]
      val authService = stub[AuthService]
      authService.authenticate _ when * returns Future(None)
      val contactRoute = new ContactRoute(contactService, new Directives(authService))
      Post(
        "/contact/list",
        HttpEntity(MediaTypes.`application/json`, "anyContent")
      ) ~> addHeader("Token", "invalidToken") ~> contactRoute.route ~> check {
        handled shouldBe false
      }
      (contactService.list _).expects(*) never()
    }
    "Reject mark creation request on missing token" in {
      val contactService = mock[ContactService]
      val authService = mock[AuthService]
      val contactRoute = new ContactRoute(contactService, new Directives(authService))
      Post(
        "/contact/list",
        HttpEntity(MediaTypes.`application/json`, "{anyContent}")
      ) ~> contactRoute.route ~> check {
        handled shouldBe false
      }
      (contactService.list _).expects(*) never()
      (authService.authenticate _).expects(*) never()
    }
  }
}

