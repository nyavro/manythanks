package com.eny.manythanks.mark

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MarkController extends Controller {

   private case class MarkRequest(from:String, to:String, up:Boolean, message:String)

   private val form = Form(
     mapping(
       "from" -> text,
       "to" -> text,
       "up" -> boolean,
       "message" -> text
     )(MarkRequest.apply)(MarkRequest.unapply)
   )

   def create = Action.async(parse.json) {
     implicit request =>
       form.bindFromRequest.fold(
         formWithErrors => Future(BadRequest("Invalid json")),
         markRequest => {
           println(s"Mark request from:${markRequest.from} to:${markRequest.to} up: ${markRequest.to}, message: ${markRequest.message}")
           Future(Ok("Success"))
         }
       )
   }
 }
