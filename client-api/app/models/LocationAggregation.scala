package models

import play.api.libs.json.Json
import play.api.libs.json.Format
import play.api.libs.json.{JsResult, JsValue}
import play.api.libs.json.JsSuccess

final class LocationAggregation(
    val location: String,
    val valid: Long,
    val failed: Long
)

object LocationAggregation {

    implicit object LocationAggregationFormat extends Format[LocationAggregation] {

        // convert LocationAggregation to Json (serialise)
        def writes(agg:LocationAggregation) = Json.obj(
            "location" -> agg.location,
            "valid" -> agg.valid,
            "failed" -> agg.failed
        )

        // TODO implement deserialisation
        override def reads(json: JsValue): JsResult[LocationAggregation] = {
            JsSuccess(new LocationAggregation("", 0, 0))
        }
    }
}