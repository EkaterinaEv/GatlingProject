package vc

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

import scala.util.Random

object CommonScenario {
  def apply(): ScenarioBuilder = new CommonScenario().scn
}

class CommonScenario {

  private val citiesList = List("Denver", "Frankfurt", "London", "Los Angeles", "Paris",
    "Portland", "San Francisco", "Seattle", "Sydney", "Zurich")

  private val cityPairs = Iterator.continually {
    val shuffled = Random.shuffle(citiesList)
    Map(
      "departCity" -> shuffled.head,
      "arriveCity" -> shuffled.tail.head
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
    .pause(1, 3)
    .exec(Actions.selectFlight)
    .pause(1, 3)
    .exec(Actions.buyTicket)
    .pause(1, 3)
    .exec(Actions.goHome)

}