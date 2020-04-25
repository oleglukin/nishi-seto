package controllers

import javax.inject._
import models.LocationAggregation
import play.api._
import play.api.mvc._
import play.api.libs.json.Json

@Singleton
class LocationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
    
    val tempLocation = new LocationAggregation("myTestLocation", 941, 223)

    def aggregationByLocation(locationname: String) = Action {
        val locationJson = Json.toJson(tempLocation)
        // toJson(data) relies on existence of
        // `Writes[List[Repo]]` type class in scope
        Ok(locationJson)
    }

    def getListOfKnownLocations() = Action {
        val locations = List("Iiyama", "Onomichi", "Odaiba")
        val locationsJson = Json.toJson(locations)
        Ok(locationsJson)
    }
}
