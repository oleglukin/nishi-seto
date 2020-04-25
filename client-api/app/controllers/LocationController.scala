package controllers

import javax.inject._
import models.LocationAggregation
import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import scala.collection.mutable

@Singleton
class LocationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
    
    val aggMap = mutable.Map.empty[String,LocationAggregation]


    def getListOfKnownLocations() = Action {
        val locationsJson = Json.toJson(aggMap.keySet)
        Ok(locationsJson)
    }

    def getAggregationByLocation(locationname: String) = Action {
        if (aggMap.contains(locationname)) {
            val locationJson = Json.toJson(aggMap(locationname))
            Ok(locationJson)
        }
        else {
            NotFound(locationname)
        }
    }

    def getAllAggregations() = Action {
        val locationJson = Json.toJson(aggMap.toList)
        Ok(locationJson)
    }

    def getTotalEvents() = Action {
        val total = aggMap.values.map(x => x.failed + x.valid).sum
        Ok(total.toString())
    }

    def newLocationAggregation(locationname: String) = Action {
        aggMap += (locationname -> new LocationAggregation(locationname, 941, 223))
        Ok
    }

    def removeAggregation(locationname: String) = Action {
        aggMap.remove(locationname) match {
            case Some(value) => Ok(s"Removed aggregation for '${value.location}'")
            case None => NotFound(locationname)
        }
    }

    def clearAllAggregations() = Action {
        aggMap.clear
        Ok
    }
}
