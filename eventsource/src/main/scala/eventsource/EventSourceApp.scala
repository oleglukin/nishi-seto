package eventsource

object EventSourceApp {
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
    val maxeventSourceIds = 3;




  }


  def getLocations(maxLocations: Int): Seq[String] = {
    (0 to (maxLocations - 1)).foreach()
  }
  
}
