package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.SignalEvent

@Singleton
class SignalController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def newSignalEvent() = Action { request =>
    request.body.asJson match {
      case Some(jsonValue) => {
        val parseResult = Json.fromJson[SignalEvent](jsonValue)
        parseResult match {
          case JsSuccess(value, path) => {
            println(s"Got signal event from request:\nlocation: ${value.location}\nattribute: ${value.attribute}")
            // TODO handle signal event
            Ok
          }
          case JsError(errors) => {
            BadRequest("Detected error:" + JsError(errors.take(0).toString()))
          }
        }
      }
      case None => BadRequest("Expecting Json data")
    }
  }
}