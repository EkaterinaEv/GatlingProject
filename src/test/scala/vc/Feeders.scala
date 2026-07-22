package vc

import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder

object Feeders {

  val users: BatchableFeederBuilder[String] = csv("users.csv").circular
  val passengers: BatchableFeederBuilder[String] = csv("passengers.csv").circular

}
