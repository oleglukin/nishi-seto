package models

import java.time.LocalDateTime
import play.api.libs.json.Json

final case class SignalEvent(
    location: String,
    attribute: String,
    uom: String, // Unit of Measure
    value: String//,
    //timestamp: LocalDateTime
)

object SignalEvent {
    implicit val signalJsonFormat = Json.format[SignalEvent]
}