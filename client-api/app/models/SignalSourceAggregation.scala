package models

import play.api.libs.json.Json
import play.api.libs.json.Format
import play.api.libs.json.{JsResult, JsValue}
import play.api.libs.json.JsSuccess

final class SignalSourceAggregation(
    val source: String,
    val valid: Long,
    val failed: Long
)

object SignalSourceAggregation {

    implicit object SignalSourceAggregationFormat extends Format[SignalSourceAggregation] {

        // convert SignalSourceAggregation to Json (serialise)
        def writes(agg:SignalSourceAggregation) = Json.obj(
            "source" -> agg.source,
            "valid" -> agg.valid,
            "failed" -> agg.failed
        )

        // TODO implement deserialisation
        override def reads(json: JsValue): JsResult[SignalSourceAggregation] = {
            JsSuccess(new SignalSourceAggregation("", 0, 0))
        }
    }
}