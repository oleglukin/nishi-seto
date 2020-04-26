package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

@Singleton
class SignalController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def newSignalEvent() = Action { request =>
    request.body.asJson.map { json =>

      json.validate[(String, String, String, String)].map { 
        case (location, attribute, uom, value) => Ok("Hello " + location + ", you're "+attribute)
      }.recoverTotal{
        e => BadRequest("Detected error:" + JsError(e.toString()))
      }

    }.getOrElse {
      BadRequest("Expecting Json data")
    }
  }

}
