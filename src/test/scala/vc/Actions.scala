package vc

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object Actions {

  val getMainPage: HttpRequestBuilder = http("getMainPage")
    .get("/webtours/")
    .check(status.is(200))
    .check(css("title").is("Web Tours"))

  val getNavbar: HttpRequestBuilder = http("getNavbar")
    .get("/cgi-bin/nav.pl?in=home")
    .check(status.is(200))
    .check(css("title").is("Web Tours Navigation Bar"))
    .check(css("input[name='userSession']", "value").saveAs("userSession"))


  val login: HttpRequestBuilder = http("login")
    .post("/cgi-bin/login.pl")
    .formParam("userSession", "#{userSession}")
    .formParam("Username", "#{username}")
    .formParam("Password", "#{password}")
    .formParam("login.x", "62")
    .formParam("login.y", "10")
    .formParam("JSFormSubmit", "off")
    .check(status.is(200))
    .check(bodyString.is("User password was correct"))

}