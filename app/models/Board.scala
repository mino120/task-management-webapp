package models

import play.api.libs.json.{Json, OFormat}
import java.time.LocalDateTime

case class Board(id: Option[Long], 
                name: String, 
                description: Option[String], 
                userId: Long, 
                createdAt: Option[LocalDateTime] = Some(LocalDateTime.now()))

object Board {
  implicit val format: OFormat[Board] = Json.format[Board]
}