package eventsource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EventSourceAppSpec extends AnyFlatSpec with Matchers {
  "The EventSourceApp object" should "return result" in {
    EventSourceApp.func shouldEqual "result"
  }
}
