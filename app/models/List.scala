package models

import play.api.libs.json._

case class List(
  id: Option[Long], 
  name: String, 
  position: Int, 
  boardId: Long
)

object List {
  implicit val format: Format[List] = Json.format[List]
}