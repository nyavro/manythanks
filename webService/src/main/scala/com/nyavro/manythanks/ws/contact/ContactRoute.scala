package com.nyavro.manythanks.ws.contact

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.nyavro.manythanks.ws.route.RouteProvider
import com.nyavro.manythanks.ws.security.Directives
import spray.json._

import scala.concurrent.ExecutionContextExecutor


class ContactRoute(service:ContactService, directives:Directives)
  (implicit private val system:ActorSystem,
  implicit private val executor: ExecutionContextExecutor,
  implicit private val materializer: ActorMaterializer) extends RouteProvider with SprayJsonSupport with ContactFormat {

  case class ContactListRequest(extIds:List[String])
  case class ContactListResponse(list:List[Contact])

  private implicit val contactListRequest = jsonFormat1(ContactListRequest)
  private implicit val contactListResponse = jsonFormat1(ContactListResponse)

  override def route: Route = pathPrefix("contact") {
    path("list") {
      pathEndOrSingleSlash {
        directives.authenticate { loggedUser =>
          post {
            entity(as[ContactListRequest]) { item =>
              complete(OK -> service.list(item.extIds).map(ContactListResponse).map(_.toJson))
            }
          }
        }
      }
    } ~ path("test") {
      get {
        println("here")
        complete(OK)
      }
    }
  }
}
