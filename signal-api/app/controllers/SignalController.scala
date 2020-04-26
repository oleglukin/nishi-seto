package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class SignalController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  def newSignalEvent() = Action { implicit request: Request[AnyContent] =>
    Ok
  }
}
