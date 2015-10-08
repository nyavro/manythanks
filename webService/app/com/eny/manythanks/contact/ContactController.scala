package com.eny.manythanks.contact

import com.eny.manythanks.contact.ContactJSonFormat._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ContactController extends Controller {

  private case class ContactRequest(ids:List[String])

  private val form = Form(
    mapping(
      "ids" -> play.api.data.Forms.list(text)
    )(ContactRequest.apply)(ContactRequest.unapply)
  )

  def list = Action.async(parse.json) {
    implicit request =>
      form.bindFromRequest.fold(
        formWithErrors => Future(BadRequest(formWithErrors.globalErrors.map(err=>err.message).mkString(","))),
        contactRequest => {
          contactRequest.ids.foreach(id => println(s"id:$id"))
          Future(Ok("Success"))
        }
      )
  }
}
