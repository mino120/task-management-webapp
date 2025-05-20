package services

import javax.inject.{Inject, Singleton}
import models.Board
import scala.collection.mutable
import java.time.LocalDateTime
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BoardService @Inject()(implicit ec: ExecutionContext) {
  // In-memory storage
  private val boards = mutable.Map[Long, Board]()
  private var idCounter: Long = 1

  // Sample data initialization
  {
    create(Board(None, "My First Board", Some("This is a demo board"), 1))
  }

  def create(board: Board): Future[Board] = Future {
    val id = idCounter
    val newBoard = board.copy(
      id = Some(id),
      createdAt = Some(LocalDateTime.now())
    )
    boards(id) = newBoard
    idCounter += 1
    newBoard
  }

  def list(userId: Long): Future[Seq[Board]] = Future {
    boards.values.filter(_.userId == userId).toSeq
  }

  def get(id: Long): Future[Option[Board]] = Future {
    boards.get(id)
  }

  def update(board: Board): Future[Option[Board]] = Future {
    board.id match {
      case Some(id) if boards.contains(id) =>
        val updated = board.copy(
          createdAt = boards(id).createdAt // Preserve original creation date
        )
        boards(id) = updated
        Some(updated)
      case _ => None
    }
  }

  def delete(id: Long): Future[Boolean] = Future {
    if (boards.contains(id)) {
      boards.remove(id)
      true
    } else {
      false
    }
  }
}