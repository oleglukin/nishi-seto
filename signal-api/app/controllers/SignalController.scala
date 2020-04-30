package controllers

import akka.actor._
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.SignalEvent
import services.SignalHandler

@Singleton
class SignalController @Inject()(val controllerComponents: ControllerComponents, system: ActorSystem) extends BaseController {

  val signalHandler = system.actorOf(SignalHandler.props, "signal-handler")

  def newSignalEvent() = Action { request =>
    request.body.asJson match {
      case Some(jsonValue) => {
        val parseResult = Json.fromJson[SignalEvent](jsonValue)
        parseResult match {
          case JsSuccess(value, path) => {
            signalHandler ! value
            Ok // TODO Internal Error if cannot add event to processing stream
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