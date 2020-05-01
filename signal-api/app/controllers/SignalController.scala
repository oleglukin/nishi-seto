package controllers

import akka.actor._
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.SignalEvent
import services.SignalHandler
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit

@Singleton
class SignalController @Inject()(val controllerComponents: ControllerComponents, system: ActorSystem) extends BaseController {

  val signalHandler = system.actorOf(SignalHandler.props, "signal-handler")

  //This will schedule to send the Tick-message
  //to the SignalHandler actor every 5 seconds
  import system.dispatcher
  val cancellable =
    system.scheduler.scheduleAtFixedRate(
      FiniteDuration(0, TimeUnit.MILLISECONDS),
      FiniteDuration(5, TimeUnit.SECONDS),
      signalHandler,
      "tick")

  def newSignalEvent() = Action { request =>
    request.body.asJson match {
      case Some(jsonValue) => {
        val parseResult = Json.fromJson[SignalEvent](jsonValue)
        parseResult match {
          case JsSuccess(value, path) => {
            signalHandler ! value
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