package models

import java.time.LocalDateTime
import play.api.libs.json.Json

final case class SignalEvent(
    source: String, // signal source id/name, e.g. ZXCP_HRT059NC
    attribute: String, // attribute name, e.g. temperature or engine speed
    uom: String,    // Unit of Measure, e.g. rpm, percentage
    value: String   // e.g. 16, 954.12, or "read failure"
    //timestamp: LocalDateTime
)

object SignalEvent {
    implicit val signalJsonFormat = Json.format[SignalEvent]

    def toJson(e: SignalEvent) = Json.toJson(e).toString
}