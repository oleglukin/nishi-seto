package eventsource

import scala.collection.immutable.NumericRange
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

    val events = 16
    val maxeventSourceIds = 10
    val maxIntervalMs = 5

    // TODO read config from file

    val sourcesIds = getSources(maxeventSourceIds)

    print(s"${LocalDateTime.now}\nGenerating $events events for ${sourcesIds.length} sources. Max interval between requests: $maxIntervalMs ms\nEvent sources:")
    sourcesIds.foreach(sid => print(sid + " "))
    println
    if (events > 1000) println("Events generation progress x1000:")

    // there is no asynchronouse or parallel execution on purpose
    (1 to events).foreach(i => {
      val json = generateRandomEvent(sourcesIds, attributesConfig, i)

      println(json)

      Thread.sleep(getRandomInt(maxIntervalMs))


      if (events > 1000 && i%1000 == 0) {
        print(s"${i/1000} ");
      }
    })
  }


  def getSources(maxSources: Int) = (0 to (maxSources - 1)).map(_ match {
    case a if a%3 == 0 => "EX" + getStringUpperCase(2) + "_" + getStringUpperCase(8)
    case b => "TR" + getString(2, ('D' to 'R')) + "_" + getStringUpperCase(3) + getStringNumeric(3) + getStringUpperCase(2)
  })

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
}