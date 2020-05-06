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

    def getFilePath = exchangeFolder + fileNameFromDate(LocalDateTime.now)

    def fileNameFromDate(d:LocalDateTime) = 
        s"${d.getYear}-${intToStr(d.getMonthValue, 2)}-${intToStr(d.getDayOfMonth, 2)}" +
        s"_${intToStr(d.getHour, 2)}-${intToStr(d.getMinute, 2)}-${intToStr(d.getSecond, 2)}.txt"

    def intToStr(n:Int, lengt:Int) = {
        val str = n.toString
        str.length match {
            case less if less < lengt => "0" * (lengt - less) + str
            case _ => str
        }
    }
}