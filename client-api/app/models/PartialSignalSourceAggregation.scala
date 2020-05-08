package models

import play.api.libs.json.Json

final case class PartialSignalSourceAggregation(
  val source: String,
  val valid: Boolean,
  val count: Long
)

object PartialSignalSourceAggregation {
  implicit val signalAggJsonFormat = Json.format[PartialSignalSourceAggregation]
}