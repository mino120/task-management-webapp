package controllers
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import services.{BoardService, ListService}
import models.Board
import scala.concurrent.{ExecutionContext, Future}
import play.api.data._
import play.api.data.Forms._

@Singleton
class BoardController @Inject()(
  val controllerComponents: ControllerComponents,
  boardService: BoardService,
  listService: ListService // Added ListService injection
)(implicit ec: ExecutionContext) extends BaseController {
  // Existing JSON API methods
  def list(userId: Long) = Action.async { implicit request =>
    boardService.list(userId).map { boards =>
      Ok(Json.toJson(boards))
    }
  }
  
  def get(id: Long) = Action.async { implicit request =>
    boardService.get(id).map {
      case Some(board) => Ok(Json.toJson(board))
      case None => NotFound(Json.obj("message" -> s"Board with id $id not found"))
    }
  }
  
  def create() = Action.async(parse.json) { implicit request =>
    request.body.validate[Board].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> "Invalid board data", "errors" -> JsError.toJson(errors))))
      },
      board => {
        boardService.create(board).map { created =>
          Created(Json.toJson(created))
        }
      }
    )
  }
  
  def update(id: Long) = Action.async(parse.json) { implicit request =>
    request.body.validate[Board].fold(
      errors => {
        Future.successful(BadRequest(Json.obj("message" -> "Invalid board data", "errors" -> JsError.toJson(errors))))
      },
      board => {
        val boardToUpdate = board.copy(id = Some(id))
        boardService.update(boardToUpdate).map {
          case Some(updated) => Ok(Json.toJson(updated))
          case None => NotFound(Json.obj("message" -> s"Board with id $id not found"))
        }
      }
    )
  }
  
  def delete(id: Long) = Action.async { implicit request =>
    boardService.delete(id).map { deleted =>
      if (deleted) NoContent
      else NotFound(Json.obj("message" -> s"Board with id $id not found"))
    }
  }
  
  def listAll(userId: Long) = Action.async { implicit request =>
    boardService.list(userId).map { boards =>
      Ok(views.html.boardList(boards)) // Updated to use a boardList view
    }
  }
  
  def showCreateForm() = Action { implicit request =>
    Ok(views.html.createBoard())
  }
  
  // Form definition - moved outside the createBoard method
  private val boardForm = Form(
    mapping(
      "name" -> nonEmptyText(minLength = 3, maxLength = 50),
      "description" -> text
    )(BoardFormData.apply)(BoardFormData.unapply)
  )
  
  // Form data class - moved outside the createBoard method
  case class BoardFormData(name: String, description: String)
  
  // Updated to create default lists
  def createBoard() = Action.async(parse.formUrlEncoded) { implicit request =>
    boardForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.createBoard()))
      },
      boardData => {
        val board = Board(
          id = None,
          name = boardData.name,
          description = Some(boardData.description),
          userId = 1 // Using default user ID for simplicity
        )
        // First create the board
        boardService.create(board).flatMap { createdBoard =>
          val boardId: Long = createdBoard.id.getOrElse(0L) // Add explicit typing and use 0L
          // Then create the three default lists
          val toDoList = models.List(None, "To-Do", 0, boardId)
          val doingList = models.List(None, "Doing", 1, boardId)
          val doneList = models.List(None, "Done", 2, boardId)
          // Create all three lists
          for {
            _ <- listService.create(toDoList)
            _ <- listService.create(doingList)
            _ <- listService.create(doneList)
          } yield {
            Redirect(routes.HomeController.board(boardId))
              .flashing("success" -> "Board created successfully")
          }
        }
      }
    )
  }
}