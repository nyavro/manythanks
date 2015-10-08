package com.eny.manythanks.contact

import play.api.libs.json.Json
import play.api.mvc.{PathBindable, Action, Controller}
import com.eny.manythanks.contact.ContactJSonFormat._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class ContactController extends Controller {

  def list(ids:List[String]) = Action.async {
    Future(ids.map(id => Contact("Vasy", Some(id))))
      .map {
        Json.arr(_)
      }.map {
        items => Ok(items(0))
      }
  }
}
