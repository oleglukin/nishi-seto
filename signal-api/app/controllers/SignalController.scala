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
class SignalController @Inject()(val controllerComponents: ControllerComponents, system: ActorSystem, config: Configuration) extends BaseController {

  val signalOutputFolder = config.get[String]("signal.fileDestinationFolder")
  val dumpScheduleSec = config.get[Int]("signal.schedule.dump.duration.sec")
  val clearFolderScheduleSec = config.get[Long]("signal.schedule.clearFolder.duration.sec")
  
  val props = Props(classOf[SignalHandler], signalOutputFolder, clearFolderScheduleSec)
  val signalHandler = system.actorOf(props, "signal-handler")

  // This will schedule to send messages (dump, clear)
  // to the SignalHandler actor every n seconds
  import system.dispatcher
  val cancellableDump =
    system.scheduler.scheduleAtFixedRate(
      FiniteDuration(0, TimeUnit.MILLISECONDS),
      FiniteDuration(dumpScheduleSec, TimeUnit.SECONDS),
      signalHandler,
      "dump")

  val cancellableClearFolder =
    system.scheduler.scheduleAtFixedRate(
      FiniteDuration(0, TimeUnit.MILLISECONDS),
      FiniteDuration(clearFolderScheduleSec, TimeUnit.SECONDS),
      signalHandler,
      "clear folder")


  /**
    * Parse signal json value and send it to SignalHandler actor
    */
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