package com.nyavro.manythanks.gcm

import java.net.{HttpURLConnection, URL}

import org.apache.commons.io.IOUtils
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

class Transport {
  val Message = "message"
  val To = "to"
  val Data = "data"
  val GCM = "https://android.googleapis.com/gcm/send"
  val API_KEY = "AIzaSyDVV5tut_3_WCfBR4HgoQmcH-zN2_QIlrE"
  val sampleToken =
    "dQNhIKBYag4:APA91bGtZ1POzF21mPfKnLlNEfxShc4vz36Qjv2gGmjbZUtLa4a9jDIiIqIfFjcOrh-40EBrsY_MXXjeIuS2UDwNT6-BkxGoTevQr5mvtvUcQv9ISoaCju4QycdbOxDaG_bYJ5XIT_lO"
  def composeMessage(message:String, target:String):Array[Byte] = {
    val json =
      (To -> target) ~
      (Data -> (Message -> message))
    println(compact(render(json)))
    compact(render(json)).getBytes
  }

  def send(message:String, target:String) = {
    try {
      val url = new URL(GCM)
      val conn = url.openConnection().asInstanceOf[HttpURLConnection]
      conn.setRequestProperty("Authorization", "key=" + API_KEY)
      conn.setRequestProperty("Content-Type", "application/json")
      conn.setRequestMethod("POST")
      conn.setDoOutput(true)
      conn.getOutputStream.write(composeMessage(message, target))
      println("result:" + IOUtils.toString(conn.getInputStream))
    } catch {
      case e:Throwable => println(e.getLocalizedMessage)
      case _ => println("wrong")
    }
  }
}

object Transport extends Transport {
  def main(args: Array[String]): Unit = {
//    send("Привет лунатикам!!", "/topics/global")
    send("Привет лунатикам!!", sampleToken)
  }
}
