package models

import java.time.LocalDateTime

final case class SignalEvent(
    location: String,
    attribute: String,
    uom: String, // Unit of Measure
    value: String,
    timestamp: LocalDateTime
)