package com.nyavro.manythanks.ws.mark

case class Mark(id:Option[Long], extId:String, from:Long, to:Long, up:Boolean, message:String)