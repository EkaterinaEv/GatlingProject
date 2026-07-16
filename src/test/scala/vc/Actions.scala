package vc

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

object Actions {

  val getMainPage: HttpRequestBuilder = http("getMainPage")
    .get("/webtours/")
    .check(status.is(200))
    .check(css("title").is("Web Tours"))

  // Создаем сессию и получаем MSO cookie
  val createSession: HttpRequestBuilder = http("createSession")
    .get("/cgi-bin/welcome.pl?signOff=true")
    .check(status.is(200))
    // Сохраняем MSO cookie для использования в следующих запросах
    .check(headerRegex("Set-Cookie", "MSO=([^;]+)").saveAs("msoCookie"))

  // Загружаем навигацию и извлекаем userSession из скрытого поля формы
  val getNavbar: HttpRequestBuilder = http("getNavbar")
    .get("/cgi-bin/nav.pl?in=home")
    .check(status.is(200))
    .check(css("title").is("Web Tours Navigation Bar"))
    // Извлекаем userSession из скрытого поля input
    .check(css("input[name='userSession']", "value").saveAs("userSession"))

  val login: HttpRequestBuilder = http("login")
    .post("/cgi-bin/login.pl")
    .formParam("userSession", "#{userSession}")
    .formParam("username", "#{username}")
    .formParam("password", "#{password}")
    .formParam("login.x", "69")
    .formParam("login.y", "3")
    .formParam("JSFormSubmit", "off")
    .check(status.is(200))
    .check(regex("User password was correct").exists)
    .check(css("title").is("Web Tours"))


}