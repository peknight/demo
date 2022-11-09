package com.peknight.demo.frontend.heima.pink.webapis

import com.peknight.demo.frontend.channg.easylazyload.{EasyLazyLoad, LazyLoadOptions}
import com.peknight.demo.frontend.heima.pink.webapis.AnimateScript.animate
import org.querki.jquery.*
import org.scalajs.dom
import scalatags.JsDom.all.*

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportTopLevel, JSGlobal}
import scala.util.matching.Regex

object PinyougouScript:

  @JSExportTopLevel("pinyougouIndex")
  def pinyougouIndex(): Unit =
    val regex: Regex = ("(?i)(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|" +
      "Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)").r
    if regex.findFirstMatchIn(dom.window.navigator.userAgent).isDefined then dom.window.location.href = "/jingdong"

  @JSExportTopLevel("pinyougouFocus")
  def pinyougouFocus(): Unit =
    dom.window.addEventListener("load", _ => {
      val arrowL = dom.document.querySelector(".arrow-l").asInstanceOf[dom.HTMLElement]
      val arrowR = dom.document.querySelector(".arrow-r").asInstanceOf[dom.HTMLElement]
      val focus = dom.document.querySelector(".focus").asInstanceOf[dom.HTMLElement]
      val focusWidth = focus.offsetWidth
      var timerOption: Option[Int] = Some(dom.window.setInterval(() => arrowR.click(), 2000))
      focus.addEventListener("mouseenter", _ => {
        arrowL.style.display = "block"
        arrowR.style.display = "block"
        timerOption.foreach(dom.window.clearInterval)
        timerOption = None
      })
      focus.addEventListener("mouseleave", _ => {
        arrowL.style.display = "none"
        arrowR.style.display = "none"
        timerOption = Some(dom.window.setInterval(() => arrowR.click(), 2000))
      })
      val ulElement = focus.querySelector("ul").asInstanceOf[dom.HTMLElement]
      val olElement = focus.querySelector(".circle").asInstanceOf[dom.HTMLElement]
      olElement.appendChild(frag(List.fill(ulElement.children.length)(li())).render)
      ulElement.appendChild(ulElement.children.head.cloneNode(true))
      val imgCount = ulElement.children.length
      val circles = olElement.children
      circles.head.classList.add("current")
      var imgIndex = 0
      var circleIndex = 0
      circles.zipWithIndex.foreach {
        case (circle, index) => circle.addEventListener("click", _ => {
          circles.foreach(_.classList.remove("current"))
          circle.classList.add("current")
          imgIndex = index
          circleIndex = index
          animate(ulElement, -index * focusWidth)
        })
      }
      var flag = true
      arrowR.addEventListener("click", _ => {
        if flag then
          flag = false
          if imgIndex == imgCount - 1 then
            ulElement.style.left = "0"
            imgIndex = 0
          imgIndex += 1
          animate(ulElement, -imgIndex * focusWidth, () => flag = true)
          circleIndex += 1
          if circleIndex == circles.length then circleIndex = 0
          circleChange(circles, circleIndex)
      })
      arrowL.addEventListener("click", _ => {
        if flag then
          flag = false
          if imgIndex == 0 then
            imgIndex = imgCount - 1
            ulElement.style.left = s"${-imgIndex * focusWidth}px"
          imgIndex -= 1
          animate(ulElement, -imgIndex * focusWidth, () => flag = true)
          circleIndex -= 1
          if circleIndex < 0 then circleIndex = circles.length - 1
          circleChange(circles, circleIndex)
      })
    })

  private[this] def circleChange(circles: dom.HTMLCollection[dom.Element], circleIndex: Int): Unit =
    circles.foreach(_.classList.remove("current"))
    circles(circleIndex).classList.add("current")

  @JSExportTopLevel("pinyougouFixedTool")
  def pinyougouFixedTool(): Unit =
    $(() => {
      var flag = true
      val toolTop = $(".recom").offset().top
      val fixedTool = $(".fixed-tool")
      def toggleTool(): Unit =
        if $(dom.document).scrollTop() >= toolTop then fixedTool.fadeIn()
        else fixedTool.fadeOut()
      toggleTool()
      $(dom.window).scroll(() => {
        toggleTool()
        if flag then $(".floor .w").each((element: dom.Element, index: Int) => {
          if $(dom.document).scrollTop() >= $(element).offset().top then
            $(".fixed-tool li").eq(index).addClass("current").siblings().removeClass("current")
        })
      })
      $(".fixed-tool li").click((element: dom.Element) => {
        flag = false
        val target = $(element)
        // 当我们每次点击li，就需要计算出页面要去往的位置
        // 选出对应索引号的内容区的盒子 计算它的offset().top
        val current = $(".floor .w").eq(target.index()).offset().top
        // 页面动画滚动效果
        $("body, html").stop().animate(
          js.Dictionary[js.Any]("scrollTop" -> current),
          1000, "",
          _ => flag = true
        )
        target.addClass("current").siblings().removeClass("current")
      })
    })

  @JSExportTopLevel("pinyougouLazyLoad")
  def pinyougouLazyLoad(): Unit =
    EasyLazyLoad.lazyLoadInit(LazyLoadOptions(showTime = 500))


  @JSExportTopLevel("pinyougouDetail")
  def pinyougouDetail(): Unit =
    dom.window.addEventListener("load", _ => {
      val previewImg = dom.document.querySelector(".preview-img").asInstanceOf[dom.HTMLElement]
      val mask = previewImg.querySelector(".mask").asInstanceOf[dom.HTMLElement]
      val big = previewImg.querySelector(".big").asInstanceOf[dom.HTMLElement]
      previewImg.addEventListener("mouseover", _ => {
        mask.style.display = "block"
        big.style.display = "block"
      })
      previewImg.addEventListener("mouseout", _ => {
        mask.style.display = "none"
        big.style.display = "none"
      })
      previewImg.addEventListener[dom.MouseEvent]("mousemove", e => {
        // 先计算鼠标在盒子内的坐标
        val x = e.pageX - previewImg.offsetLeft
        val y = e.pageY - previewImg.offsetTop
        val maskXMax = previewImg.offsetWidth - mask.offsetWidth
        val maskYMax = previewImg.offsetHeight - mask.offsetHeight
        val maskX = math.min(math.max(x - mask.offsetWidth / 2, 0), maskXMax)
        val maskY = math.min(math.max(y - mask.offsetHeight / 2, 0), maskYMax)
        mask.style.left = s"${maskX}px"
        mask.style.top = s"${maskY}px"
        val bigImg = big.querySelector(".big-img").asInstanceOf[dom.HTMLElement]
        val bigXMax = bigImg.offsetWidth - big.offsetWidth
        val bigYMax = bigImg.offsetHeight - big.offsetHeight
        val bigX = maskX * bigXMax / maskXMax
        val bigY = maskY * bigYMax / maskYMax
        bigImg.style.left = s"${-bigX}px"
        bigImg.style.top = s"${-bigY}px"
      })
    })

  @JSExportTopLevel("pinyougouCart")
  def pinyougouCart(): Unit =
    $(() => {
      $(".check-all").change((element: dom.Element) => {
        val checked = $(element).prop("checked").asInstanceOf[Boolean]
        $(".j-checkbox, .check-all").prop("checked", checked)
        val cartItem = $(".cart-item")
        if checked then cartItem.addClass("check-cart-item") else cartItem.removeClass("check-cart-item")
      })
      $(".j-checkbox").change((element: dom.Element) => {
        val checkAll = $(".check-all")
        if $(".j-checkbox:checked").length == $(".j-checkbox").length then checkAll.prop("checked", true)
        else checkAll.prop("checked", false)
        val target = $(element)
        if target.prop("checked").asInstanceOf[Boolean] then target.parents(".cart-item").addClass("check-cart-item")
        else target.parents(".cart-item").removeClass("check-cart-item")
      })
      $(".increment").click((element: dom.Element) => {
        val target = $(element)
        val iTxt = target.siblings(".i-txt")
        val num: Int = iTxt.value().asInstanceOf[String].toInt + 1
        updatePSum(target, iTxt, num)
      })
      $(".decrement").click((element: dom.Element) => {
        val target = $(element)
        val iTxt = target.siblings(".i-txt")
        val num: Int = iTxt.value().asInstanceOf[String].toInt - 1
        if num > 0 then updatePSum(target, iTxt, num)
      })
      $(".i-txt").change((element: dom.Element) => {
        val target = $(element)
        val num = target.value().asInstanceOf[String].toInt
        updatePSum(target, target, num)
      })
      updateSum()
      $(".p-action a").click((element: dom.Element) => {
        $(element).parents(".cart-item").remove()
        updateSum()
      })
      $(".remove-batch").click(() => {
        $(".j-checkbox:checked").parents(".cart-item").remove()
        updateSum()
      })
      $(".clear-all").click(() => {
        $(".cart-item").remove()
        updateSum()
      })
    })

  def updatePSum(target: JQuery, iTxt: JQuery, num: Int): Unit =
    iTxt.value(s"$num")
    val pNum = target.parents(".p-num")
    val price: Double = pNum.siblings(".p-price").html().tail.toDouble
    pNum.siblings(".p-sum").html("￥%.2f".format(price * num))
    updateSum()

  def updateSum(): Unit =
    var count: Int = 0
    var money: Double = 0
    $(".i-txt").each((ele: dom.Element) => count += $(ele).value().asInstanceOf[String].toInt)
    $(".amount-sum em").text(s"$count")
    $(".p-sum").each((ele: dom.Element) => money += $(ele).text().tail.toDouble)
    $(".price-sum em").text("￥%.2f".format(money))
