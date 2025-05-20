package services
import javax.inject.{Inject, Singleton}
import models.Task
import scala.collection.mutable
import java.time.LocalDateTime
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaskService @Inject()(
  listService: ListService  // Inject ListService to get lists by board
)(implicit ec: ExecutionContext) {
  // In-memory storage
  private val tasks = mutable.Map[Long, Task]()
  private var idCounter: Long = 1
  
  // Sample data initialization
  {
    create(Task(None, "Learn Scala", Some("Study Scala programming language basics"), 1, 0, None, false))
    create(Task(None, "Create Task App", Some("Build a simple task management application"), 1, 1, None, false))
    create(Task(None, "Setup Project", Some("Initialize project structure"), 2, 0, None, false))
    create(Task(None, "Complete Assignment", Some("Submit the final project"), 3, 0, None, false))
  }
  
  def create(task: Task): Future[Task] = Future {
    val id = idCounter
    val newTask = task.copy(
      id = Some(id),
      createdAt = Some(LocalDateTime.now())
    )
    tasks(id) = newTask
    idCounter += 1
    newTask
  }
  
  def listByBoardList(listId: Long): Future[Seq[Task]] = Future {
    tasks.values
      .filter(_.listId == listId)
      .toSeq
      .sortBy(_.position)
  }
  
  // New method to list all tasks for a board by getting tasks for all lists in the board
  def listByBoard(boardId: Long): Future[Seq[Task]] = {
    for {
      lists <- listService.listByBoard(boardId)
      listIds = lists.flatMap(_.id) // Extract list IDs
      allTasks <- Future.successful(
        tasks.values
          .filter(task => listIds.contains(task.listId))
          .toSeq
          .sortBy(t => (t.listId, t.position))
      )
    } yield allTasks
  }
  
  def get(id: Long): Future[Option[Task]] = Future {
    tasks.get(id)
  }
  
  def update(task: Task): Future[Option[Task]] = Future {
    task.id match {
      case Some(id) if tasks.contains(id) =>
        val updated = task.copy(
          createdAt = tasks(id).createdAt // Preserve original creation date
        )
        tasks(id) = updated
        Some(updated)
      case _ => None
    }
  }
  
  def delete(id: Long): Future[Boolean] = Future {
    if (tasks.contains(id)) {
      tasks.remove(id)
      true
    } else {
      false
    }
  }
}