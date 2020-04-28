package controllers

import javax.inject._
import models.SignalSourceAggregation
import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import scala.collection.mutable

@Singleton
class SignalSourceController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
    
    val aggMap = mutable.Map.empty[String,SignalSourceAggregation]


    def getListOfKnownSources() = Action {
        val sourcesJson = Json.toJson(aggMap.keySet)
        Ok(sourcesJson)
    }

    def getAggregationBySource(sourcename: String) = Action {
        if (aggMap.contains(sourcename)) {
            val sourceJson = Json.toJson(aggMap(sourcename))
            Ok(sourceJson)
        }
        else {
            NotFound(sourcename)
        }
    }

    def getAllAggregations() = Action {
        val sourceJson = Json.toJson(aggMap.toList)
        Ok(sourceJson)
    }

    def getTotalEvents() = Action {
        val total = aggMap.values.map(x => x.failed + x.valid).sum
        Ok(total.toString())
    }

    def newSourceAggregation(sourcename: String) = Action {
        aggMap += (sourcename -> new SignalSourceAggregation(sourcename, 941, 223))
        Ok
    }

    def removeAggregation(sourcename: String) = Action {
        aggMap.remove(sourcename) match {
            case Some(value) => Ok(s"Removed aggregation for '${value.source}'")
            case None => NotFound(sourcename)
        }
    }

    def clearAllAggregations() = Action {
        aggMap.clear
        Ok
    }
}
