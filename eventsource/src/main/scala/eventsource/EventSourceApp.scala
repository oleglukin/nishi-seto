package eventsource

import scala.collection.immutable.NumericRange

object EventSourceApp {
  val chLower = ('a' to 'z')
  val chUpper = ('A' to 'Z')
  val chNum = ('0' to '9')
  val chAlphaNum = chLower ++ chUpper ++ chNum

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

    getSources(maxeventSourceIds).foreach(println)


  }


  def getSources(maxSources: Int) = 
    (0 to (maxSources - 1)).map(_ match {
      case a if a%3 == 0 => getStringUpperCase(3) + "_" + getStringUpperCase(8)
      case b => getString(4, ('D' to 'R')) + "_" + getStringUpperCase(3) + getStringNumeric(3) + getStringUpperCase(2)
    })

  def getString(length: Int, chars: NumericRange[Char]): String = {
    //val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')

    (0 to (length - 1)).map(i => {
      chars(util.Random.nextInt(chars.length))
    }).mkString
  }

  def getStringLowerCase(length: Int): String = getString(length, chLower)
  def getStringUpperCase(length: Int): String = getString(length, chUpper)
  def getStringNumeric(length: Int): String = getString(length, chNum)
  
}