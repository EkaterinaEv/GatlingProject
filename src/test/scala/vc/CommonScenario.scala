package vc

import io.gatling.core.Predef._
import io.gatling.core.structure._

object CommonScenario {
  def apply(): ScenarioBuilder = new CommonScenario().scn
}

class CommonScenario {

  val scn: ScenarioBuilder = scenario("Common scenario")
    .feed(Feeders.users)
    .exec(Actions.getMainPage)
    .pause(1, 3)
    .exec(Actions.getNavbar)
    .pause(1, 3)
    .exec(Actions.login)

}
