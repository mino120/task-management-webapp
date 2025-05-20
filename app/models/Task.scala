package models

import play.api.libs.json._
import java.time.LocalDateTime

case class Task(
  id: Option[Long],
  title: String,
  description: Option[String],
  listId: Long,
  position: Int,
  dueDate: Option[LocalDateTime] = None,
  completed: Boolean = false,
  createdAt: Option[LocalDateTime] = None
)

object Task {
  implicit val format: Format[Task] = Json.format[Task]
}