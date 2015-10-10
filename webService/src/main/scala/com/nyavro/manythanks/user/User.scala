package com.nyavro.manythanks.user

case class User(id:Option[Long], extId:String, login:String, password:String) {
  require(!login.isEmpty, "login.empty")
  require(!password.isEmpty, "password.empty")
}
