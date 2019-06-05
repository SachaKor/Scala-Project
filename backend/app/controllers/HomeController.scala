package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter

import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, securedAction: SecuredAction) extends AbstractController(cc) {

  /*
  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
        routes.javascript.StudentsController.getStudents,
        routes.javascript.StudentsController.createStudent,
        routes.javascript.StudentsController.getStudent,
        routes.javascript.StudentsController.updateStudent,
        routes.javascript.StudentsController.deleteStudent,
        routes.javascript.CoursesController.getCourses,
        routes.javascript.CoursesController.createCourse,
        routes.javascript.CoursesController.getCourse,
        routes.javascript.CoursesController.updateCourse,
        routes.javascript.CoursesController.deleteCourse
      )
    ).as("text/javascript")
  }
  */

  def index = Action {
    Ok(
      Json.obj(
      "status"  -> "OK",
      "message" -> "Welcome to the Youglouf API"
    ))
  }

  def secure = securedAction.async {
    implicit request => {
      Future.successful(
        Ok(
          Json.obj(
            "status" -> "OK",
            "message" -> s"You are VIP ${request.userInfo.firstName}"
          )
        )
      )
    }
  }

}
