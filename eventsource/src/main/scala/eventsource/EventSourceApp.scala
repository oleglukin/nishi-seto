package eventsource

import scala.collection.immutable.NumericRange
import scalaj.http._
import java.time.LocalDateTime
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._


object EventSourceApp {
  val chUpper = ('A' to 'Z')
  val chNum = ('0' to '9')

  def main(args: Array[String]): Unit = {

    // attribute name, uom, min value, max value
    val attributesConfig = IndexedSeq(
      ("engine speed", "rpm", 0, 1400),
      ("engine temperature", "celsius", 0, 600),
      ("oil level", "percentage", 0, 100),
      ("battery voltage", "volts", 0, 20),
      ("coolant level", "percentage", 0, 100),
      ("vechicle speed", "mk/h", 0, 200)
    )

    val events = getEvents(args)
    val maxEventSourceIds = getMaxEventSourceIds(args)
    val maxIntervalMs = getMaxIntervalMs(args)
    val url = getUrl(args)

    val sourcesIds = getSources(maxEventSourceIds)

    print(s"${LocalDateTime.now}\nGenerating $events events for ${sourcesIds.length} sources. Max interval between requests: $maxIntervalMs ms\nEvent sources:")
    sourcesIds.foreach(sid => print(sid + " "))
    println
    if (events > 1000) println("Events generation progress x1000:")

    // there is no asynchronouse or parallel execution on purpose
    (1 to events).foreach(i => {
      if (maxIntervalMs > 0) Thread.sleep(getRandomInt(maxIntervalMs))

      val json = generateRandomEvent(sourcesIds, attributesConfig, i)

      Http(url).postData(json).header("content-type", "application/json").asString.code match {
        case 200 => ()
        case code => println(s"Post response: $code")
      }

      if (events > 1000 && i%1000 == 0) print(s"${i/1000} ");
    })

    print(s"${LocalDateTime.now}\nFinished posting all events")
  }


  def getSources(maxSources: Int) = (0 to (maxSources - 1)).map(_ match {
    case a if a%3 == 0 => "EX" + getStringUpperCase(2) + "_" + getStringUpperCase(8)
    case b => "TR" + getString(2, ('D' to 'R')) + "_" + getStringUpperCase(3) + getStringNumeric(3) + getStringUpperCase(2)
  })

  // functions returning random values
  def getString(length: Int, chars: IndexedSeq[Char]) = (1 to length).map(_ => getRandomChar(chars)).mkString
  def getRandomInt(max: Int) = util.Random.nextInt(max)
  def getRandomIntMinMax(min: Int, max: Int) = getRandomInt(max - min) + min
  def getRandomChar(chars: IndexedSeq[Char]) = chars(getRandomInt(chars.length))
  def getStringUpperCase(length: Int) = getString(length, chUpper)
  def getStringNumeric(length: Int) = getString(length, chNum)
  def getStringUpperCaseAlphaNumeric(length: Int) = getString(length, (chUpper ++ chNum))

  def generateRandomEvent(
    sourcesIds: IndexedSeq[String],
    attributesConfig: IndexedSeq[(String, String, Int, Int)],
    i: Int
    ): String = {
    val source = sourcesIds(getRandomInt(sourcesIds.length))
    val attrTuple = attributesConfig(getRandomInt(attributesConfig.length))
    val value = getRandomIntMinMax(attrTuple._3, attrTuple._4)
    val json = ("source" -> source)~("attribute" -> attrTuple._1)~("uom" -> attrTuple._2)~("value" -> value.toString)
    compact(render(json))
  }

  def getEvents(args: Array[String]) = args.length match {
    case l if l >= 1 => args(0).toInt
    case default => 16
  }

  def getMaxEventSourceIds(args: Array[String]) = args.length match {
    case l if l >= 2 => args(1).toInt
    case default => 10
  }

  def getMaxIntervalMs(args: Array[String]) = args.length match {
    case l if l >= 3 => args(2).toInt
    case default => 0
  }

  def getUrl(args: Array[String]) = args.length match {
    case l if l >= 4 => args(3)
    case default => "http://localhost:9000/api/signal"
  }
}