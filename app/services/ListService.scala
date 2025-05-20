package services

import javax.inject.{Inject, Singleton}
import models.{List => BoardList}
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ListService @Inject()(implicit ec: ExecutionContext) {
  // In-memory storage
  private val lists = mutable.Map[Long, BoardList]()
  private var idCounter: Long = 1

  // Sample data initialization
  {
    create(BoardList(None, "To Do", 1, 0))
    create(BoardList(None, "In Progress", 1, 1))
    create(BoardList(None, "Done", 1, 2))
  }

  def create(list: BoardList): Future[BoardList] = Future {
    val id = idCounter
    val newList = list.copy(id = Some(id))
    lists(id) = newList
    idCounter += 1
    newList
  }

  def listByBoard(boardId: Long): Future[Seq[BoardList]] = Future {
    lists.values
      .filter(_.boardId == boardId)
      .toSeq
      .sortBy(_.position)
  }

  def get(id: Long): Future[Option[BoardList]] = Future {
    lists.get(id)
  }

  def update(list: BoardList): Future[Option[BoardList]] = Future {
    list.id match {
      case Some(id) if lists.contains(id) =>
        lists(id) = list
        Some(list)
      case _ => None
    }
  }

  def delete(id: Long): Future[Boolean] = Future {
    if (lists.contains(id)) {
      lists.remove(id)
      true
    } else {
      false
    }
  }
}