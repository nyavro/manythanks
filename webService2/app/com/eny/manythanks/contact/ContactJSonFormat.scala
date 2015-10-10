package com.eny.manythanks.contact

import play.api.libs.json.Json

object ContactJSonFormat {
  implicit val format = Json.format[Contact]
}