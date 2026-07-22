package vc

import io.gatling.core.Predef._

import scala.concurrent.duration._


class QuickCheck extends Simulation {

  // Пробный тест на 2 ступени по 1 минуте
  private val STEP_USERS = 10
  private val STEP_DURATION = 1.minute

  private val scn = CommonScenario()

  setUp(
    scn.inject(
      // Разгон
      rampConcurrentUsers(0).to(5).during(10.seconds),

      // Ступень 1: 10 пользователей
      rampConcurrentUsers(0).to(STEP_USERS).during(10.seconds),
      constantConcurrentUsers(STEP_USERS).during(STEP_DURATION),

      // Ступень 2: 20 пользователей
      rampConcurrentUsers(0).to(STEP_USERS * 2).during(10.seconds),
      constantConcurrentUsers(STEP_USERS * 2).during(STEP_DURATION)
    ).protocols(httpProtocol)
  )
    .maxDuration(5.minutes)
    .assertions(
      global.failedRequests.percent.lt(10),
      global.responseTime.mean.lt(5000)
    )

}
