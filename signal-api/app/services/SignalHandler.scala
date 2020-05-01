package services

import akka.actor._
import javax.inject._
import models.SignalEvent

class SignalHandler extends Actor {

    override def receive = {
        case e: SignalEvent => println(s"${e.source} ${e.attribute} ${e.uom}, ${e.value}")
        case "tick" => println("++got tick")
    }
}


object SignalHandler {
    def props = Props[SignalHandler]
}