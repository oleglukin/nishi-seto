package models

import java.time.LocalDateTime
import play.api.libs.json.Json

final case class SignalEvent(
    source: String, // signal source id/name
    attribute: String,
    uom: String, // Unit of Measure
    value: String//,
    //timestamp: LocalDateTime
)

object SignalEvent {
    implicit val signalJsonFormat = Json.format[SignalEvent]
}