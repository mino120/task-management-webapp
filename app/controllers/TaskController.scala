package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import services.{TaskService, ListService}
import models.Task
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaskController @Inject()(
  val controllerComponents: ControllerComponents,
  taskService: TaskService,
  listService: ListService  // Added ListService injection
)(implicit ec: ExecutionContext) extends BaseController {

  def listByList(listId: Long) = Action.async { implicit request =>
    taskService.listByBoardList(listId).map { tasks =>
      Ok(Json.toJson(tasks))
    }
  }

  def get(id: Long) = Action.async { implicit request =>
    taskService.get(id).map {
      case Some(task) => Ok(Json.toJson(task))
      case None => NotFound(Json.obj("message" -> s"Task with id $id not found"))
    }
  }

  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[Task].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> "Invalid task data", "errors" -> JsError.toJson(errors))))
      },
      task => {
        taskService.create(task).map { created =>
          Created(Json.toJson(created))
        }
      }
    )
  }

  def update(id: Long) = Action.async(parse.json) { implicit request =>
    request.body.validate[Task].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> "Invalid task data", "errors" -> JsError.toJson(errors))))
      },
      task => {
        val taskToUpdate = task.copy(id = Some(id))
        taskService.update(taskToUpdate).map {
          case Some(updated) => Ok(Json.toJson(updated))
          case None => NotFound(Json.obj("message" -> s"Task with id $id not found"))
        }
      }
    )
  }

  def delete(id: Long) = Action.async { implicit request =>
    taskService.delete(id).map { deleted =>
      if (deleted) NoContent
      else NotFound(Json.obj("message" -> s"Task with id $id not found"))
    }
  }

  // Added form handling method for task creation
  def createTask() = Action.async(parse.formUrlEncoded) { implicit request =>
    val listId = request.body.get("listId").flatMap(_.headOption).map(_.toLong).getOrElse(0L)
    val title = request.body.get("title").flatMap(_.headOption).getOrElse("")
    val description = request.body.get("description").flatMap(_.headOption).getOrElse("")
    
    // Get list to find board ID
    listService.get(listId).flatMap {
      case Some(list) =>
        val task = Task(
          id = None,
          title = title,
          description = Some(description),
          listId = listId,
          position = 0,  // Add at top position
          dueDate = None,
          completed = false
        )
        
        taskService.create(task).map { _ =>
          Redirect(routes.HomeController.board(list.boardId))
            .flashing("success" -> "Task added successfully")
        }
      case None =>
        Future.successful(BadRequest("List not found"))
    }
  }
}