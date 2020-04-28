package eventsource

import scala.collection.immutable.NumericRange

object EventSourceApp {
  val chUpper = ('A' to 'Z')
  val chNum = ('0' to '9')

  def main(args: Array[String]): Unit = {

    // TODO read config from file
    // attribute name, uom, min value, max value
    val attributesConfig = Seq(
      ("engine speed", "rpm", 0, 1400),
      ("engine temperature", "celsius", 0, 600),
      ("oil level", "percentage", 0, 100),
      ("battery voltage", "volts", 0, 20),
      ("coolant level", "percentage", 0, 100),
      ("vechicle speed", "mk/h", 0, 200)
    )

    val events = 16;
    val maxeventSourceIds = 10;

    val sourcesIds = getSources(maxeventSourceIds)

    sourcesIds.foreach(println)

  }


  def getSources(maxSources: Int) = 
    (0 to (maxSources - 1)).map(_ match {
      case a if a%3 == 0 => "EX" + getStringUpperCase(2) + "_" + getStringUpperCase(8)
      case b => "TR" + getString(2, ('D' to 'R')) + "_" + getStringUpperCase(3) + getStringNumeric(3) + getStringUpperCase(2)
    })

  def getString(length: Int, chars: IndexedSeq[Char]) = (1 to length).map(_ => getRandomChar(chars)).mkString
  def getRandomChar(chars: IndexedSeq[Char]) = chars(util.Random.nextInt(chars.length))
  def getStringUpperCase(length: Int) = getString(length, chUpper)
  def getStringNumeric(length: Int) = getString(length, chNum)
  def getStringUpperCaseAlphaNumeric(length: Int) = getString(length, (chUpper ++ chNum))
}