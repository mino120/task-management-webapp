package controllers

import javax.inject._
import play.api.mvc._
import services.{BoardService, ListService, TaskService}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(
  val controllerComponents: ControllerComponents,
  boardService: BoardService,
  listService: ListService,
  taskService: TaskService
)(implicit ec: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def board(id: Long) = Action.async { implicit request =>
    for {
      boardOpt <- boardService.get(id)
      lists <- listService.listByBoard(id)
      tasks <- taskService.listByBoard(id)
    } yield {
      boardOpt match {
        case Some(board) =>
          // Group tasks by listId
          val tasksByList = tasks.groupBy(_.listId)
          // Ensure your template signature matches these parameters:
          // (board: models.Board, lists: Seq[models.List], tasksByList: Map[Long, Seq[models.Task]])(implicit request: RequestHeader)
          Ok(views.html.board(board, lists, tasksByList))
        case None =>
          NotFound("Board not found")
      }
    }
  }
}
