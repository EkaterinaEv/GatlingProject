package vc

import io.gatling.core.Predef._

import scala.concurrent.duration._

class ReliabilityTest extends Simulation {

  // 80% от максимума (если максимум 100 пользователей)
  private val TARGET_USERS = 80
  private val TEST_DURATION = 1.hour

  private val scn = CommonScenario()

  setUp(
    scn.inject(
      // Разогрев
      rampConcurrentUsers(0).to(TARGET_USERS).during(5.minutes),
      // Постоянная нагрузка 1 час
      constantConcurrentUsers(TARGET_USERS).during(TEST_DURATION)
    ).protocols(httpProtocol)
  )
    .maxDuration(TEST_DURATION + 10.minutes)
    .assertions(
      global.failedRequests.percent.lt(1),
      global.responseTime.mean.lt(2000)
    )
}