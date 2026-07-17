package vc

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random

object CommonScenario {
  def apply(): ScenarioBuilder = new CommonScenario().scn
}

class CommonScenario {

  private val citiesList = List("Denver", "Frankfurt", "London", "Los Angeles", "Paris",
    "Portland", "San Francisco", "Seattle", "Sydney", "Zurich")

  private val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  private def getRandomDates(): (String, String) = {
    val today = LocalDate.now()
    // Дата отправления - сегодня + 1 день
    val departDate = today.plusDays(1)
    // Дата возврата - дата отправления + 15 дней
    val returnDate = departDate.plusDays(15)
    (departDate.format(dateFormatter), returnDate.format(dateFormatter))
  }

  private val cityPairs = Iterator.continually {
    val shuffled = Random.shuffle(citiesList)
    val (departDate, returnDate) = getRandomDates()
    Map(
      "departCity" -> shuffled.head,
      "arriveCity" -> shuffled.tail.head,
      "departDate" -> departDate,
      "returnDate" -> returnDate
    )
  }

  val scn: ScenarioBuilder = scenario("Common scenario")
    .feed(Feeders.users)
    .feed(cityPairs)
    .exec(Actions.getMainPage)
    .pause(1, 3)
    .exec(Actions.createSession)
    .pause(1, 3)
    .exec(Actions.getNavbar)
    .pause(1, 3)
    .exec(Actions.login)
    .pause(1, 3)
    .exec(Actions.goToFlights)
    .pause(1, 3)
    .exec(Actions.goToReservation)
    .pause(1, 3)
    .exec(Actions.findFlight)
    .exec(session => {
      val flights = session("flightsList").as[List[String]]
      val randomFlight = Random.shuffle(flights).head
      println(s"Available flights: ${flights.mkString(", ")}")
      println(s"Selected flight: $randomFlight")
      session.set("outboundFlight", randomFlight)
    })
    .pause(1, 3)
    .exec(Actions.selectFlight)
    .pause(1, 3)
    .exec(Actions.buyTicket)
    .pause(1, 3)
    .exec(Actions.goHome)

}