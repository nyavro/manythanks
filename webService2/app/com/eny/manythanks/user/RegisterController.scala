package com.eny.manythanks.user

import com.eny.manythanks.contact.ContactJSonFormat._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RegisterController extends Controller {

  private case class RegisterRequest(uid:String, id:String)

  private val form = Form(
    mapping(
      "uid" -> text,
      "id" -> text
    )(RegisterRequest.apply)(RegisterRequest.unapply)
  )

  def register = Action.async(parse.json) {
    implicit request =>
      form.bindFromRequest.fold(
        formWithErrors => Future(BadRequest("")),
        registerRequest => {
          println(s"Registration request: ${registerRequest.id}")
          Future(Ok("Success"))
        }
      )
  }
}
