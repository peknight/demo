package com.peknight.demo.frontend.heima.pink.echarts

import com.peknight.demo.frontend.apache.echarts.`export`.EChartsOption
import com.peknight.demo.frontend.apache.echarts.chart.effectscatter.{EffectScatterDataItemOption, EffectScatterEmphasisOption, EffectScatterSeriesOption}
import com.peknight.demo.frontend.apache.echarts.chart.helper.{LineDrawEffectOption, RippleEffectOption}
import com.peknight.demo.frontend.apache.echarts.chart.lines.{LinesDataItemOption, LinesLineStyleOption, LinesSeriesOption}
import com.peknight.demo.frontend.apache.echarts.util.{CallbackDataParams, ItemStyleOption, OptionDataItemObject, SeriesLabelOption, SeriesOption}
import com.peknight.demo.frontend.apache.echarts.chart.pie.*
import com.peknight.demo.frontend.apache.echarts.component.tooltip.{TooltipOption, TopLevelFormatterParams}
import com.peknight.demo.frontend.apache.echarts.coord.geo.{GeoEmphasisOption, GeoItemStyleOption, GeoLabelOption, GeoOption}
import com.peknight.demo.frontend.apache.echarts.core.ECharts
import org.querki.jquery.*
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object DataVisualizationScript:

  given [A]: CanEqual[js.UndefOr[A], A] = CanEqual.derived

  @JSExportTopLevel("dataVisualization")
  def dataVisualization(): Unit = $(() => {
    $(".monitor .tabs").on("click", "a", null, (element: dom.Element) => {
      $(element).addClass("active").siblings("a").removeClass("active")
      $(".monitor .content").eq($(element).index()).show().siblings(".content").hide()
    })
    $(".marquee-view .marquee").each((element: dom.Element) => {
      val rows = $(element).children().clone()
      $(element).append(rows)
    })
    ECharts.init(dom.document.querySelector(".pie").asInstanceOf[dom.HTMLElement]).setOption(EChartsOption(
      tooltip = TooltipOption(trigger = "item", formatter = "{a} <br/>{b} : {c} ({d}%)"),
      color = js.Array("#006cff", "#60cda0", "#ed8884", "#ff9f7f", "#0096ff", "#9fe6b8", "#32c5e9", "#1d9dff"),
      series = js.Array(PieSeriesOption(
        name = "点位统计",
        radius = js.Array("10%", "70%"),
        center = js.Array("50%", "50%"),
        roseType = "area",
        itemStyle = PieItemStyleOption(borderRadius = 5),
        data = js.Array(
          PieDataItemOption(value = 20, name = "云南"),
          PieDataItemOption(value = 26, name = "北京"),
          PieDataItemOption(value = 24, name = "山东"),
          PieDataItemOption(value = 25, name = "河北"),
          PieDataItemOption(value = 20, name = "江苏"),
          PieDataItemOption(value = 25, name = "浙江"),
          PieDataItemOption(value = 30, name = "四川"),
          PieDataItemOption(value = 42, name = "湖北"),
        ),
        label = PieLabelOption(fontSize = 10),
        labelLine = PieLabelLineOption(length = 6, length2 = 8)
      ))
    ))
  })

  @JSExportTopLevel("dataVisualizationChinaMap")
  def dataVisualizationChinaMap(): Unit = $(() => {
    val geoChart = ECharts.init(dom.document.querySelector(".geo").asInstanceOf[dom.HTMLElement])
    val series = js.Array(
      convertSeries("西安", xianData, "#a6c84c") :::
        convertSeries("西宁", xiningData, "#ffa022") :::
        convertSeries("拉萨", lasaData, "#46bee9")*
    )
    val option = EChartsOption(
      tooltip = TooltipOption(
        trigger = "item",
        formatter = (params: TopLevelFormatterParams, _: String, _: js.Function2[String, String | dom.HTMLElement | js.Array[dom.HTMLElement], Unit]) => {
          dom.console.log(params)
          val cb: CallbackDataParams = params.asInstanceOf[CallbackDataParams]
          cb.seriesType match
            case "effectScatter" =>
              val data: EffectScatterDataItemOption = cb.data.asInstanceOf[EffectScatterDataItemOption]
              s"线路：${data.name} ${data.value.asInstanceOf[js.Array[Double | Int]](2)}"
            case "lines" =>
              val data: LinesDataItemOption = cb.data.asInstanceOf[LinesDataItemOption]
              s"${data.fromName} > ${data.toName} <br /> ${data.value}"
            case _ => cb.name
        }
      ),
      geo = GeoOption(
        map = "china",
        emphasis = GeoEmphasisOption(
          label = GeoLabelOption(show = true, color = "#fff"),
          itemStyle = GeoItemStyleOption(areaColor = "#2B91B7")
        ),
        roam = true,
        zoom = 1.2,
        itemStyle = GeoItemStyleOption(areaColor = "#142957", borderColor = "#195BB9", borderWidth = 1)
      ),
      series = series
    )
    dom.console.log(option)
    geoChart.setOption(option)
    dom.window.addEventListener("resize", _ => geoChart.resize())
  })

  val geoCoordMap: Map[String, js.Array[Double | Int]] = Map(
    "上海" -> js.Array(121.4648, 31.2891),
    "东莞" -> js.Array(113.8953, 22.901),
    "东营" -> js.Array(118.7073, 37.5513),
    "中山" -> js.Array(113.4229, 22.478),
    "临汾" -> js.Array(111.4783, 36.1615),
    "临沂" -> js.Array(118.3118, 35.2936),
    "丹东" -> js.Array(124.541, 40.4242),
    "丽水" -> js.Array(119.5642, 28.1854),
    "乌鲁木齐" -> js.Array(87.9236, 43.5883),
    "佛山" -> js.Array(112.8955, 23.1097),
    "保定" -> js.Array(115.0488, 39.0948),
    "兰州" -> js.Array(103.5901, 36.3043),
    "包头" -> js.Array(110.3467, 41.4899),
    "北京" -> js.Array(116.4551, 40.2539),
    "北海" -> js.Array(109.314, 21.6211),
    "南京" -> js.Array(118.8062, 31.9208),
    "南宁" -> js.Array(108.479, 23.1152),
    "南昌" -> js.Array(116.0046, 28.6633),
    "南通" -> js.Array(121.1023, 32.1625),
    "厦门" -> js.Array(118.1689, 24.6478),
    "台州" -> js.Array(121.1353, 28.6688),
    "合肥" -> js.Array(117.29, 32.0581),
    "呼和浩特" -> js.Array(111.4124, 40.4901),
    "咸阳" -> js.Array(108.4131, 34.8706),
    "哈尔滨" -> js.Array(127.9688, 45.368),
    "唐山" -> js.Array(118.4766, 39.6826),
    "嘉兴" -> js.Array(120.9155, 30.6354),
    "大同" -> js.Array(113.7854, 39.8035),
    "大连" -> js.Array(122.2229, 39.4409),
    "天津" -> js.Array(117.4219, 39.4189),
    "太原" -> js.Array(112.3352, 37.9413),
    "威海" -> js.Array(121.9482, 37.1393),
    "宁波" -> js.Array(121.5967, 29.6466),
    "宝鸡" -> js.Array(107.1826, 34.3433),
    "宿迁" -> js.Array(118.5535, 33.7775),
    "常州" -> js.Array(119.4543, 31.5582),
    "广州" -> js.Array(113.5107, 23.2196),
    "廊坊" -> js.Array(116.521, 39.0509),
    "延安" -> js.Array(109.1052, 36.4252),
    "张家口" -> js.Array(115.1477, 40.8527),
    "徐州" -> js.Array(117.5208, 34.3268),
    "德州" -> js.Array(116.6858, 37.2107),
    "惠州" -> js.Array(114.6204, 23.1647),
    "成都" -> js.Array(103.9526, 30.7617),
    "扬州" -> js.Array(119.4653, 32.8162),
    "承德" -> js.Array(117.5757, 41.4075),
    "拉萨" -> js.Array(91.1865, 30.1465),
    "无锡" -> js.Array(120.3442, 31.5527),
    "日照" -> js.Array(119.2786, 35.5023),
    "昆明" -> js.Array(102.9199, 25.4663),
    "杭州" -> js.Array(119.5313, 29.8773),
    "枣庄" -> js.Array(117.323, 34.8926),
    "柳州" -> js.Array(109.3799, 24.9774),
    "株洲" -> js.Array(113.5327, 27.0319),
    "武汉" -> js.Array(114.3896, 30.6628),
    "汕头" -> js.Array(117.1692, 23.3405),
    "江门" -> js.Array(112.6318, 22.1484),
    "沈阳" -> js.Array(123.1238, 42.1216),
    "沧州" -> js.Array(116.8286, 38.2104),
    "河源" -> js.Array(114.917, 23.9722),
    "泉州" -> js.Array(118.3228, 25.1147),
    "泰安" -> js.Array(117.0264, 36.0516),
    "泰州" -> js.Array(120.0586, 32.5525),
    "济南" -> js.Array(117.1582, 36.8701),
    "济宁" -> js.Array(116.8286, 35.3375),
    "海口" -> js.Array(110.3893, 19.8516),
    "淄博" -> js.Array(118.0371, 36.6064),
    "淮安" -> js.Array(118.927, 33.4039),
    "深圳" -> js.Array(114.5435, 22.5439),
    "清远" -> js.Array(112.9175, 24.3292),
    "温州" -> js.Array(120.498, 27.8119),
    "渭南" -> js.Array(109.7864, 35.0299),
    "湖州" -> js.Array(119.8608, 30.7782),
    "湘潭" -> js.Array(112.5439, 27.7075),
    "滨州" -> js.Array(117.8174, 37.4963),
    "潍坊" -> js.Array(119.0918, 36.524),
    "烟台" -> js.Array(120.7397, 37.5128),
    "玉溪" -> js.Array(101.9312, 23.8898),
    "珠海" -> js.Array(113.7305, 22.1155),
    "盐城" -> js.Array(120.2234, 33.5577),
    "盘锦" -> js.Array(121.9482, 41.0449),
    "石家庄" -> js.Array(114.4995, 38.1006),
    "福州" -> js.Array(119.4543, 25.9222),
    "秦皇岛" -> js.Array(119.2126, 40.0232),
    "绍兴" -> js.Array(120.564, 29.7565),
    "聊城" -> js.Array(115.9167, 36.4032),
    "肇庆" -> js.Array(112.1265, 23.5822),
    "舟山" -> js.Array(122.2559, 30.2234),
    "苏州" -> js.Array(120.6519, 31.3989),
    "莱芜" -> js.Array(117.6526, 36.2714),
    "菏泽" -> js.Array(115.6201, 35.2057),
    "营口" -> js.Array(122.4316, 40.4297),
    "葫芦岛" -> js.Array(120.1575, 40.578),
    "衡水" -> js.Array(115.8838, 37.7161),
    "衢州" -> js.Array(118.6853, 28.8666),
    "西宁" -> js.Array(101.4038, 36.8207),
    "西安" -> js.Array(109.1162, 34.2004),
    "贵阳" -> js.Array(106.6992, 26.7682),
    "连云港" -> js.Array(119.1248, 34.552),
    "邢台" -> js.Array(114.8071, 37.2821),
    "邯郸" -> js.Array(114.4775, 36.535),
    "郑州" -> js.Array(113.4668, 34.6234),
    "鄂尔多斯" -> js.Array(108.9734, 39.2487),
    "重庆" -> js.Array(107.7539, 30.1904),
    "金华" -> js.Array(120.0037, 29.1028),
    "铜川" -> js.Array(109.0393, 35.1947),
    "银川" -> js.Array(106.3586, 38.1775),
    "镇江" -> js.Array(119.4763, 31.9702),
    "长春" -> js.Array(125.8154, 44.2584),
    "长沙" -> js.Array(113.0823, 28.2568),
    "长治" -> js.Array(112.8625, 36.4746),
    "阳泉" -> js.Array(113.4778, 38.0951),
    "青岛" -> js.Array(120.4651, 36.3373),
    "韶关" -> js.Array(113.7964, 24.7028)
  )
  val xianData = Seq(
    ("西安", "北京", 100),
    ("西安", "上海", 100),
    ("西安", "广州", 100),
    ("西安", "西宁", 100),
    ("西安", "拉萨", 100)
  )
  val xiningData = Seq(
    ("西宁", "北京", 100),
    ("西宁", "上海", 100),
    ("西宁", "广州", 100),
    ("西宁", "西安", 100),
    ("西宁", "银川", 100)
  )
  val lasaData = Seq(
    ("拉萨", "北京", 100),
    ("拉萨", "潍坊", 100),
    ("拉萨", "哈尔滨", 100)
  )
  val planePath = "path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63." +
    "961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12." +
    "238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12." +
    "238-241.308L1705.06,1318.313z"

  def convertData(data: Seq[(String, String, Int)]): js.Array[LinesDataItemOption] = js.Array(
    data.map { case (from, to, value) =>
      for
        fromCoord <- geoCoordMap.get(from)
        toCoord <- geoCoordMap.get(to)
      yield
        LinesDataItemOption(fromName = from, toName = to, coords = js.Array(fromCoord, toCoord), value = value)
    }.collect {
      case Some(linesDataItemOption) => linesDataItemOption
    }*
  )

  def convertSeries(city: String, data: Seq[(String, String, Int)], color: String): List[SeriesOption[_, _, _, _]] =
    val name = s"$city Top3"
    val convertedData = convertData(data)
    List(
      LinesSeriesOption(
        name = name,
        zlevel = 1,
        effect = LineDrawEffectOption(show = true, period = 6, trailLength = 0.7, color = "red", symbolSize = 3),
        lineStyle = LinesLineStyleOption(color = color, width = 0, curveness = 0.2),
        data = convertedData
      ),
      LinesSeriesOption(
        name = name,
        zlevel = 2,
        symbol = js.Array("none", "arrow"),
        symbolSize = 10,
        effect = LineDrawEffectOption(show = true, period = 6, trailLength = 0, symbol = planePath, symbolSize = 15),
        lineStyle = LinesLineStyleOption(color = color, width = 1, opacity = 0.6, curveness = 0.2),
        data = convertedData
      ),
      EffectScatterSeriesOption(
        name = name,
        coordinateSystem = "geo",
        zlevel = 2,
        rippleEffect = RippleEffectOption(brushType = "stroke"),
        label = SeriesLabelOption(show = true, position = "right", formatter = "{b}"),
        symbolSize = (value, _) => {
          value.asInstanceOf[js.Array[Double | Int]](2) match
            case d: Double => d / 8
            case i: Int => i / 8
        },
        itemStyle = ItemStyleOption(color = color),
        emphasis = EffectScatterEmphasisOption(GeoItemStyleOption(areaColor = "#2B91B7")),
        data = js.Array(data.map {
          case (_, to, value) => EffectScatterDataItemOption(name = to, value = geoCoordMap(to).concat(js.Array(value)))
        }*)
      )
    )
