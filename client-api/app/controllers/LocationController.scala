package controllers

import javax.inject._
import models.LocationAggregation
import play.api._
import play.api.mvc._
import play.api.libs.json._

@Singleton
class LocationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
    
    val tempLocation = new LocationAggregation("myTestLocation", 941, 223)

    // Typeclass for converting LocationAggregation -> JSON
    implicit val writesLocationAggregation = new Writes[LocationAggregation] {
        def writes(agg:LocationAggregation) = Json.obj(
            "location" -> agg.location,
            "valid" -> agg.valid,
            "failed" -> agg.failed
        )
    }


    def aggregationByLocation(locationname: String) = Action {
        val locationJson = Json.toJson(tempLocation)
        // toJson(data) relies on existence of
        // `Writes[List[Repo]]` type class in scope
        Ok(locationJson)
    }
}
