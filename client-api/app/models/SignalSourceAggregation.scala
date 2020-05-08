package models

import play.api.libs.json.{Json, Format, JsResult, JsValue, JsSuccess}

final case class SignalSourceAggregation(
  val source: String,
  var valid: Long,
  var failed: Long
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