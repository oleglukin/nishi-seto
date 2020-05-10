package services

import akka.actor._
import javax.inject._
import models.SignalEvent
import scala.collection.mutable
import java.io._
import java.time.LocalDateTime
import java.nio.file.{Files,FileSystems}
import java.nio.file.attribute.FileTime
import java.util.concurrent.TimeUnit

/**
  * This actor processes signal events:
  * - accumulate in memeory for a while
  * - dump accumulated events to a text file
  * - remove old files once in a while
  * @param exchangeFolder - folder path to save files to
  * @param retireFilesOlderThanSec - remove files older than this number of seconds
  */
class SignalHandler(exchangeFolder: String, retireFilesOlderThanSec: Long) extends Actor {

  val accumulatedEvents = mutable.ArrayBuffer.empty[SignalEvent]
  
  override def receive = {
    case e: SignalEvent => accumulatedEvents += e
    case "dump" => dumpEventsToFile
    case "clear folder" => removeOldFiles(exchangeFolder, retireFilesOlderThanSec)
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

  def getListOfFiles(dir: String): List[String] = {
    val file = new File(dir)
    file.listFiles.filter(_.isFile).map(_.getPath).toList
  }

  def removeOldFiles(path: String, thresholdSed: Long) = 
    getListOfFiles(path).foreach(removeIfOlderThan(_, thresholdSed))

  def removeIfOlderThan(filePath: String, thresholdSed: Long) = {
    val path = FileSystems.getDefault().getPath(filePath)
    val modified = Files.getLastModifiedTime(path)

    val modifiedSec = modified.to(TimeUnit.SECONDS)
    val now = FileTime.fromMillis(System.currentTimeMillis).to(TimeUnit.SECONDS)

    (now - modifiedSec) match {
      case old if old > thresholdSed => new File(filePath).delete()
      case _ => ()
    }
  }
}