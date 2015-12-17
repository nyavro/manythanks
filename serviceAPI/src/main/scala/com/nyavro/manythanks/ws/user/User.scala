package com.nyavro.manythanks.ws.user

case class User(id:Option[Long], extId:String, login:String, gcmToken:String) {
  require(!login.isEmpty, "login.empty")
  require(!gcmToken.isEmpty, "gcmToken.empty")
}
