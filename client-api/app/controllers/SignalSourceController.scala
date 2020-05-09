package controllers

import javax.inject._
import models.SignalSourceAggregation
import play.api.mvc._
import play.api.libs.json.{Json, JsSuccess, JsError}
import scala.collection.mutable

@Singleton
class SignalSourceController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  val aggMap = mutable.Map.empty[String,SignalSourceAggregation]


  def getListOfKnownSources = Action {
    val sourcesJson = Json.toJson(aggMap.keySet)
    Ok(sourcesJson)
  }

  def getAggregationBySource(source: String) = Action {
    if (aggMap.contains(source)) {
      val sourceJson = Json.toJson(aggMap(source))
      Ok(sourceJson)
    }
    else NotFound(source)
  }

  def getAllAggregations = Action {
    val sourceJson = Json.toJson(aggMap.toList)
    Ok(sourceJson)
  }

  def getTotalEvents = Action {
    val failed = aggMap.values.map(x => x.failed).sum
    val valid = aggMap.values.map(x => x.valid).sum
    val total = failed + valid
    val result = "{\"valid\":" + valid + ",\"failed\":" + failed + ",\"total\":" + total + "}"
    Ok(result)
  }


  /**
    * Process new aggregation from spark job - add it to the aggMap
    */
  def newSourceAggregation(source: String, validStr: String, countStr: String) = Action {
    val valid = validStr.toBoolean
    val count = countStr.toLong

    val agg = aggMap.getOrElse(source, new SignalSourceAggregation(source, 0, 0))
    if (valid) agg.valid += count else agg.failed += count
    aggMap(source) = agg

    Ok
  }


  def removeAggregation(source: String) = Action {
    aggMap.remove(source) match {
      case Some(value) => Ok(s"Removed aggregation for '${value.source}'")
      case None => NotFound(source)
    }
  }

  def clearAllAggregations = Action {
    aggMap.clear
    Ok
  }
}