package vc

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

object CommonScenario {
  def apply(): ScenarioBuilder = new CommonScenario().scn
}

class CommonScenario {

  val scn: ScenarioBuilder = scenario("Common scenario")
    .feed(Feeders.users)
    .exec(Actions.getMainPage) // Загружаем главную страницу
    .pause(1, 3)
    .exec(Actions.createSession) // Создаем сессию (получаем MSO cookie)
    .pause(1, 3)
    .exec(Actions.getNavbar) // Загружаем навигацию и извлекаем userSession
    .pause(1, 3)
    .exec(Actions.login) // Используем userSession

}