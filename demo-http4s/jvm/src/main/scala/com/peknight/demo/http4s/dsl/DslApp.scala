package com.peknight.demo.http4s.dsl

import cats.data.NonEmptyList
import cats.effect.*
import cats.syntax.all.*
import com.comcast.ip4s.*
import com.peknight.demo.http4s.runEmberServer
import fs2.Stream
import org.http4s.*
import org.http4s.CacheDirective.`no-cache`
import org.http4s.client.dsl.io.*
import org.http4s.dsl.impl.MatrixVar
import org.http4s.dsl.io.*
import org.http4s.ember.server.*
import org.http4s.headers.`Cache-Control`
import org.http4s.implicits.*

import java.nio.charset.StandardCharsets.UTF_8
import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate, Year}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.*
import scala.util.Try

object DslApp extends IOApp.Simple:

  given CanEqual[Path, Path] = CanEqual.derived
  given CanEqual[Method, Method] = CanEqual.derived

  val service = HttpRoutes.of[IO] {
    case GET -> Root / "ok" => IO(Response(Status.Ok))
    case GET -> Root / "no-content" => NoContent()
  }

  // We can construct our own request and experiment directly in the REPL
  val getOk = Request[IO](Method.GET, uri"/ok")
  val getNoContent = Request[IO](Method.GET, uri"/no-content")

  val serviceIO = service.orNotFound.run(getOk)

  val okIo = Ok()

  val cookieResp =
    for
      resp <- Ok("Ok response.")
      now <- HttpDate.current[IO]
    yield resp.addCookie(ResponseCookie("foo", "bar", expires = Some(now), httpOnly = true, secure = true))

  val ioFuture = Ok(IO.fromFuture(IO(Future {
    println("I run when the future is constructed.")
    "Greetings from the future!"
  })))

  val io = Ok(IO {
    println("I run when the IO is run.")
    "Mission accomplished!"
  })

  val drip: Stream[IO, String] = Stream.awakeEvery[IO](100.millis).map(_.toString).take(10)
  val dripOutIO = drip.through(fs2.text.lines).evalMap(s => IO{ println(s); s }).compile.drain

  val rootRoutes = HttpRoutes.of[IO] { case GET -> Root => Ok("root") }

  // GET /hello/Alice => "Hello Alice!"
  val helloRoutes = HttpRoutes.of[IO] { case GET -> Root / "hello" / name => Ok(s"Hello $name!") }

  // 匹配任意深度的路由 如 GET /hello/Alice/Bob => "Hello, Alice and Bob!"
  // 注意这里不用Root，并且使用的是 right-associative /: extractor
  val arbitraryDepth = HttpRoutes.of[IO] {
    case GET -> "hello" /: rest => Ok(s"""Hello, ${rest.segments.mkString(" and ")}!""")
  }

  // 匹配文件扩展名 Get /weather.json => {"response": "You asked for weather"}
  val fileExtension = HttpRoutes.of[IO] {
    case GET -> Root / file ~ "json" => Ok(s"""{"response": "You asked for $file"}""")
  }

  // 匹配数字类型参数 IntVar LongVar 匹配UUID: UUIDVar
  def getUserName(userId: Int): IO[String] = IO { s"user_$userId" }
  val usersService = HttpRoutes.of[IO] {
    case GET -> Root / "users" / IntVar(userId) => Ok(getUserName(userId))
  }

  // 任意类型参数提取需要自定义模式匹配的unapply方法
  object LocalDateVar:
    def unapply(str: String): Option[LocalDate] =
      if str.nonEmpty then Try(LocalDate.parse(str)).toOption else None

  def getTemperatureForecast(date: LocalDate): IO[Double] = IO(42.23)

  val dailyWeatherService = HttpRoutes.of[IO] {
    case GET -> Root / "weather" / "temperature" / LocalDateVar(localDate) => Ok(
      getTemperatureForecast(localDate).map(temp => s"The temperature on $localDate will be: $temp")
    )
  }

  val weatherReq = GET(uri"/weather/temperature/2016-11-05")

  // Matrix Path 参数 使用MatrixVar
  object FullNameExtractor extends MatrixVar("name", List("first", "last"))
  val greetingService = HttpRoutes.of[IO] {
    case GET -> Root / "greet" / FullNameExtractor(first, last) / "greeting" => Ok(s"Hello, $first $last.")
  }
  val greeting = greetingService.orNotFound(GET(uri"/greet/name;first=john;last=doe/greeting"))

  // Matrix Path 参数里也可用IntVar这种参数
  object FullNameAndIDExtractor extends MatrixVar("name", List("first", "last", "id"))

  val greetingWithIdService = HttpRoutes.of[IO] {
    case GET -> Root / "greetwithid" / FullNameAndIDExtractor(first, last, IntVar(id)) / "greeting" =>
      Ok(s"Hello, $first $last. Your User ID is $id.")
  }
  val greetingWithId = greetingWithIdService.orNotFound(GET(uri"/greetwithid/name;first=john;last=doe;id=123/greeting"))

  // 请求参数解析
  object CountryQueryParamMatcher extends QueryParamDecoderMatcher[String]("country")

  // 改用下面处理错误的同名Decoder
  // given yearQueryParamDecoder: QueryParamDecoder[Year] = QueryParamDecoder[Int].map(Year.of)

  object YearQueryParamMatcher extends QueryParamDecoderMatcher[Year]("year")

  def getAverageTemperatureForCountryAndYear(country: String, year: Year): IO[Double] = IO(26.7)

  // GET /weather/temperature?year=2020&country=china
  val averageTemperatureService = HttpRoutes.of[IO] {
    case GET -> Root / "weather" / "temperature" :? CountryQueryParamMatcher(country) +& YearQueryParamMatcher(year) =>
      Ok(getAverageTemperatureForCountryAndYear(country, year)
        .map(temp => s"Average temperature for $country in $year was: $temp")
      )
  }

  // Instant实例编解码可以用 QueryParamCodec.instantQueryParamCodec
  given isoInstantCodec: QueryParamCodec[Instant] = QueryParamCodec.instantQueryParamCodec(DateTimeFormatter.ISO_INSTANT)

  object IsoInstantParamMatcher extends QueryParamDecoderMatcher[Instant]("timestamp")

  // Optional 请求参数
  object OptionalYearQueryParamMatcher extends OptionalQueryParamDecoderMatcher[Year]("year")

  def getAverageTemperatureForCurrentYear: IO[String] = IO("32.6")
  def getAverageTemperatureForYear(y: Year): IO[String] = IO("18.9")

  val optionalWeatherService = HttpRoutes.of[IO] {
    case GET -> Root / "temperature" :? OptionalYearQueryParamMatcher(maybeYear) =>
      maybeYear match
        case None => Ok(getAverageTemperatureForCurrentYear)
        case Some(year) => Ok(getAverageTemperatureForYear(year))
  }

  // Invalid 请求参数
  given yearQueryParamDecoder: QueryParamDecoder[Year] = QueryParamDecoder[Int]
    .emap(i => Try(Year.of(i)).toEither.leftMap(t => ParseFailure(t.getMessage, t.getMessage)))

  object YearValidatingQueryParamMatcher extends ValidatingQueryParamDecoderMatcher[Year]("year")

  val validatingWeatherService = HttpRoutes.of[IO] {
    case GET -> Root / "validate-temperature" :? YearValidatingQueryParamMatcher(yearValidated) =>
      yearValidated.fold(
        parseFailures => BadRequest("unable to parse argument year"),
        year => Ok(getAverageTemperatureForYear(year))
      )
  }

  // Optional Invalid请求参数
  object LongParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Long]("long")
  val optionalInvalidService = HttpRoutes.of[IO] {
    case GET -> Root / "number" :? LongParamMatcher(maybeNumber) =>
      val _: Option[cats.data.ValidatedNel[org.http4s.ParseFailure, Long]] = maybeNumber
      maybeNumber match
        case Some(n) => n.fold(
          parseFailures => BadRequest("unable to parse argument 'long'"),
          year => Ok(n.toString)
        )
        case None => BadRequest("missing number")
  }

  val httpApp = (rootRoutes <+>
    helloRoutes <+>
    arbitraryDepth <+>
    fileExtension <+>
    usersService <+>
    dailyWeatherService <+>
    greetingService <+>
    greetingWithIdService <+>
    averageTemperatureService <+>
    optionalWeatherService <+>
    validatingWeatherService <+>
    optionalInvalidService).orNotFound

  def run =
    for
      response <- serviceIO
      _ <- IO.println(response)
      ok <- okIo
      _ <- IO.println(ok)
      response2 <- service.orNotFound.run(getNoContent)
      _ <- IO.println(response2)
      // 看header
      okResponse <- Ok("Ok response.")
      _ <- IO.println(okResponse.headers)
      // 设置header：比如缓存控制
      headersResponse <- Ok("Ok response.", `Cache-Control`(NonEmptyList(`no-cache`(), Nil)))
      _ <- IO.println(headersResponse.headers)
      // 手写header
      headerByHand <- Ok("Ok response.", "X-Auth-Token" -> "value")
      _ <- IO.println(headerByHand.headers)
      // 设置Cookie
      cookieHeader <- Ok("Ok response.").map(_.addCookie(ResponseCookie("foo", "bar")))
      _ <- IO.println(cookieHeader.headers)
      cookieResponse <- cookieResp
      _ <- IO.println(cookieResponse.headers)
      // 删除Cookie
      removeCookie <- Ok("Ok response.").map(_.removeCookie("foo"))
      _ <- IO.println(removeCookie.headers)
      // 自动设置Content-Type
      binaryHeader <- Ok("binary".getBytes(UTF_8))
      _ <- IO.println(binaryHeader.headers)
      // 不接收参数的NoContent类型强加body将不能通过编译
      // _ <- NoContent("does not compile)
      ioFutureResponse <- ioFuture
      _ <- IO.println(ioFutureResponse)
      ioResponse <- io
      _ <- IO.println(ioResponse)
      // Streaming Bodies
      _ <- dripOutIO
      dripResponse <- Ok(drip)
      _ <- IO.println(dripResponse)
      dailyWeather <- dailyWeatherService.orNotFound(weatherReq)
      _ <- IO.println(dailyWeather)
      greetingResponse <- greeting
      _ <- IO.println(greetingResponse)
      greetingWithIdResponse <- greetingWithId
      _ <- IO.println(greetingWithIdResponse)
      _ <- runEmberServer[IO](httpApp)
    yield ()

