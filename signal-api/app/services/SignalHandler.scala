package services

import akka.actor._
import javax.inject._
import models.SignalEvent
import scala.collection.mutable
import java.io._
import java.time.LocalDateTime

class SignalHandler(exchangeFolder: String) extends Actor {

    val accumulatedEvents = mutable.ArrayBuffer.empty[SignalEvent]
    
    override def receive = {
        case e: SignalEvent => accumulatedEvents += e
        case "tick" => dumpEventsToFile
    }

    def dumpEventsToFile = {
        if (accumulatedEvents.size > 0) {
            val file = new File(getFilePath)
            val bw = new BufferedWriter(new FileWriter(file))

            accumulatedEvents.foreach(s => {
                bw.write(SignalEvent.toJson(s))
                bw.newLine
            })
            
            bw.close
            accumulatedEvents.clear
        }
    }

    def getFilePath = {
        val now = LocalDateTime.now
        s"$exchangeFolder${now.getYear}-${now.getMonthValue}-${now.getDayOfMonth()}_${now.getHour}-${now.getMinute}-${now.getSecond}.txt"
    }
}