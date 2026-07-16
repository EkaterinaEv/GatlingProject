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

  // Переход на страницу выбора рейсов
  val goToFlights: HttpRequestBuilder = http("goToFlights")
    .get("/cgi-bin/nav.pl?page=menu&in=flights")
    .check(status.is(200))
    .check(regex("Flights").exists)

  // Переход на страницу поиска рейсов
  val goToReservation: HttpRequestBuilder = http("goToReservation")
    .get("/cgi-bin/reservations.pl?page=welcome")
    .check(status.is(200))
    .check(css("title").is("Flight Selections"))

  // Поиск рейсов
  val findFlight: HttpRequestBuilder = http("findFlight")
    .post("/cgi-bin/reservations.pl")
    .formParam("advanceDiscount", "0")
    .formParam("depart", "Frankfurt")
    .formParam("departDate", "07/17/2026")
    .formParam("arrive", "Denver")
    .formParam("returnDate", "07/18/2026")
    .formParam("numPassengers", "1")
    .formParam("seatPref", "None")
    .formParam("seatType", "Coach")
    .formParam("findFlights.x", "23")
    .formParam("findFlights.y", "9")
    .formParam(".cgifields", "roundtrip")
    .formParam(".cgifields", "seatType")
    .formParam(".cgifields", "seatPref")
    .check(status.is(200))
    .check(css("title").is("Flight Selections"))
    // Сохраняем первый доступный рейс для выбора
    .check(regex("""name="outboundFlight".*?value="([^"]*)"""").saveAs("outboundFlight"))

  // Выбор рейса
  val selectFlight: HttpRequestBuilder = http("selectFlight")
    .post("/cgi-bin/reservations.pl")
    .formParam("outboundFlight", "#{outboundFlight}")
    .formParam("numPassengers", "1")
    .formParam("advanceDiscount", "0")
    .formParam("seatType", "Coach")
    .formParam("seatPref", "None")
    .formParam("reserveFlights.x", "46")
    .formParam("reserveFlights.y", "10")
    .check(status.is(200))
    .check(css("title").is("Flight Reservation"))

  // Покупка билета
  val buyTicket: HttpRequestBuilder = http("buyTicket")
    .post("/cgi-bin/reservations.pl")
    .formParam("firstName", "Kate")
    .formParam("lastName", "Evtykhova")
    .formParam("address1", "")
    .formParam("address2", "")
    .formParam("pass1", "Kate Evtykhova")
    .formParam("creditCard", "5555000066667777")
    .formParam("expDate", "03/30")
    .formParam("oldCCOption", "1")
    .formParam("numPassengers", "1")
    .formParam("seatType", "Coach")
    .formParam("seatPref", "None")
    .formParam("outboundFlight", "#{outboundFlight}")
    .formParam("advanceDiscount", "0")
    .formParam("returnFlight", "")
    .formParam("JSFormSubmit", "off")
    .formParam("buyFlights.x", "27")
    .formParam("buyFlights.y", "5")
    .formParam(".cgifields", "saveCC")
    .check(status.is(200))
    .check(css("title").is("Reservation Made!"))

  // Возврат на главную страницу
  val goHome: HttpRequestBuilder = http("goHome")
    .get("/cgi-bin/welcome.pl?page=menus")
    .check(status.is(200))
    .check(css("title").is("Web Tours"))

}