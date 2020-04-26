package services

import javax.inject._
import models.SignalEvent

@Singleton
class SignalHandler {

    def addEvent(e: SignalEvent) = {
        println(s"TODO add event to cache. ${e.location}, ${e.attribute}, ${e.uom}, ${e.value}}")
    }

}