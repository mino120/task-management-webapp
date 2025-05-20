package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import services.ListService
import models.{List => BoardList}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ListController @Inject()(
  val controllerComponents: ControllerComponents, 
  listService: ListService
)(implicit ec: ExecutionContext) extends BaseController {

  def listByBoard(boardId: Long) = Action.async { implicit request =>
    listService.listByBoard(boardId).map { lists =>
      Ok(Json.toJson(lists))
    }
  }

  def get(id: Long) = Action.async { implicit request =>
    listService.get(id).map {
      case Some(list) => Ok(Json.toJson(list))
      case None => NotFound(Json.obj("message" -> s"List with id $id not found"))
    }
  }

  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[BoardList].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> "Invalid list data", "errors" -> JsError.toJson(errors))))
      },
      list => {
        listService.create(list).map { created =>
          Created(Json.toJson(created))
        }
      }
    )
  }

  def update(id: Long) = Action.async(parse.json) { implicit request =>
    request.body.validate[BoardList].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> "Invalid list data", "errors" -> JsError.toJson(errors))))
      },
      list => {
        val listToUpdate = list.copy(id = Some(id))
        listService.update(listToUpdate).map {
          case Some(updated) => Ok(Json.toJson(updated))
          case None => NotFound(Json.obj("message" -> s"List with id $id not found"))
        }
      }
    )
  }

  def delete(id: Long) = Action.async { implicit request =>
    listService.delete(id).map { deleted =>
      if (deleted) NoContent
      else NotFound(Json.obj("message" -> s"List with id $id not found"))
    }
  }
}