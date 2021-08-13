package com.peknight.demo.rx.readme

import rx._
import rx.async.Platform._
import rx.async._
import utest._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise
import scala.concurrent.duration._
import scala.util.Failure

object ScalaRxDemo extends App {
  {
    println("-- var --")
    val a = Var(1)
    val b = Var(2)
    val c = Rx {
      a() + b()
    }
    println(c.now)
    a() = 4
    println(c.now)
  }

  {
    println("-- rx --")
    val a = Var(1)
    val b = Var(2)

    val c = Rx {
      a() + b()
    }
    val d = Rx {
      c() * 5
    }
    val e = Rx {
      c() + 4
    }
    val f = Rx {
      d() + e() + 4
    }

    println(f.now)
    a() = 3
    println(f.now)
  }

  {
    println("-- Observer trigger --")
    val a = Var(1)
    var count = 0
    val o = a.trigger {
      count = a.now + 1
    }
    println(count)
    a() = 4
    println(count)
  }

  {
    println("-- Observer foreach --")
    val a = Var(1)
    var count = 0
    val o = a.foreach { x =>
      count = x + 1
    }
    println(count)
    a() = 4
    println(count)
  }

  {
    println("-- Observer triggerLater --")
    val a = Var(1)
    var count = 0
    val o = a.triggerLater {
      count = count + 1
    }
    println(count)
    a() = 2
    println(count)
  }

  {
    println("-- kill -- ")
    val a = Var(1)
    val b = Rx {
      2 * a()
    }
    var target = 0
    val o = b.trigger {
      target = b.now
    }
    println(target)
    a() = 2
    println(target)
    o.kill()
    a() = 3
    println(target)
  }

  {
    println("-- Complex Reactives --")
    val a = Var(Seq(1, 2, 3))
    val b = Var(3)
    val c = Rx {
      b() +: a()
    }
    val d = Rx {
      c().map("omg" * _)
    }
    val e = Var("wtf")
    val f = Rx {
      (d() :+ e()).mkString
    }

    println(f.now)
    a() = Nil
    println(f.now)
    e() = "wtfbbq"
    println(f.now)
  }

  {
    println("-- Error Handling --")
    val a = Var(1)
    val b = Rx {
      1 / a()
    }
    println(b.now)
    println(b.toTry)
    a() = 0
    intercept[ArithmeticException] {
      b.now
    }
    assert(b.toTry.isInstanceOf[Failure[_]])
    println(b.toTry)
  }

  {
    println("-- exceptions propagate --")
    val a = Var(1)
    val b = Var(2)

    val c = Rx {
      a() / b()
    }
    val d = Rx {
      a() * 5
    }
    val e = Rx {
      5 / b()
    }
    val f = Rx {
      a() + b() + 2
    }
    val g = Rx {
      f() + c()
    }

    println(c.toTry)
    println(d.toTry)
    println(e.toTry)
    println(f.toTry)
    println(g.toTry)

    b() = 0

    println(c.toTry)
    println(d.toTry)
    println(e.toTry)
    println(f.toTry)
    println(g.toTry)
  }

  {
    println("-- Nesting --")
    val a = Var(1)
    val b = Rx {
      (Rx {
        a()
      }, Rx {
        math.random()
      })
    }
    val r = b.now._2.now
    println(r)
    a() = 2
    println(b.now._2.now)
  }

  {
    println("-- Nesting2 --")
    var fakeTime = 123
    trait WebPage {
      def fTime = fakeTime

      val time = Var(fTime)

      def update(): Unit = time() = fTime

      val html: Rx[String]
    }
    class HomePage(implicit ctx: Ctx.Owner) extends WebPage {
      val html = Rx {
        "Home Page! time: " + time()
      }
    }
    class AboutPage(implicit ctx: Ctx.Owner) extends WebPage {
      val html = Rx {
        "About me, time: " + time()
      }
    }
    val url = Var("www.mysite.com/home")
    val page = Rx {
      url() match {
        case "www.mysite.com/home" => new HomePage()
        case "www.mysite.com/about" => new AboutPage()
      }
    }

    println(page.now.html.now)

    fakeTime = 234
    page.now.update()
    println(page.now.html.now)

    fakeTime = 345
    url() = "www.mysite.com/about"
    println(page.now.html.now)

    fakeTime = 456
    page.now.update()
    println(page.now.html.now)
  }

  {
    var count = 0
    val a = Var(1)
    val b = Var(2)

    // 每次a变化时，c会创建一个新的Rx出来，原来的rx虽然没有引用了，但并不会消失
    def mkRxOld(i: Int) = Rx.unsafe {
      count += 1
      i + b()
    }

    // 每次a变化时，指定了Owner之后，c会先消除掉它此前创建过的rx，再创建一个新的Rx出来，原来的rx就消失了
    def mkRx(i: Int)(implicit ctx: Ctx.Owner) = Rx {
      count += 1
      i + b()
    }

    val c = Rx {
      val newRx = mkRx(a())
      newRx()
    }
    println(c.now, count)
    a() = 4
    println(c.now, count)
    b() = 3
    println(c.now, count)
    (0 to 100).foreach { i => a() = i }
    println(c.now, count)
    b() = 4
    println(c.now, count)
  }

  {
    println("-- Data Context --")
    val a = Var(1)
    val b = Var(2)
    val c = Rx {
      a.now + b.now
    }
    println(c.now)
    a() = 4
    println(c.now)
    b() = 5
    println(c.now)
  }

  {
    println("-- Map --")
    val a = Var(10)
    val b = Rx {
      a() + 2
    }
    val c = a.map(_ * 2)
    val d = b.map(_ + 3)
    println(c.now)
    println(d.now)
    a() = 1
    println(c.now)
    println(d.now)
  }

  {
    println("-- FlatMap --")
    val a = Var(10)
    val b = Var(1)
    val c = a.flatMap(a => Rx {
      a * b()
    })
    println(c.now)
    b() = 2
    println(c.now)
  }

  {
    println("-- for --")
    val a = Var(10)
    val b = for {
      aa <- a
      bb <- Rx {
        a() + 5
      }
      cc <- Var(1).map(_ * 2)
    } yield aa + bb + cc
    println(b.now)
    a() = 3
    println(b.now)
  }

  {
    println("-- Filter --")
    val a = Var(10)
    val b = a.filter(_ > 5)
    a() = 1
    println(b.now)
    a() = 6
    println(b.now)
    a() = 2
    println(b.now)
    a() = 19
    println(b.now)
  }

  {
    println("-- Filter 2 --")
    val a = Var(2)
    val b = a.filter(_ > 5)
    println(b.now)
  }

  {
    println("-- Reduce --")
    val a = Var(1)
    val b = a.reduce(_ * _)
    a() = 2
    println(b.now)
    a() = 3
    println(b.now)
    a() = 4
    println(b.now)
  }

  {
    println("-- Fold --")
    val a = Var(1)
    val b = a.fold(List.empty[Int])((acc, elem) => elem :: acc)
    a() = 2
    println(b.now)
    a() = 3
    println(b.now)
    a() = 4
    println(b.now)
  }

  {
    println("-- Future --")
    val p = Promise[Int]()
    val a = p.future.toRx(10)
    println(a.now) // 10
    p.success(5)
    println(a.now) // 10 or 5
    Thread.sleep(10)
    println(a.now) // 5
  }

  {
    println("-- Future 2 --")
    var p = Promise[Int]()
    val a = Var(1)
    val b: Rx[Int] = Rx {
      val f = p.future.toRx(10)
      f() + a()
    }
    println(b.now)
    p.success(5)
    Thread.sleep(100)
    println(b.now)

    p = Promise[Int]()
    a() = 2
    println(b.now)

    p.success(7)
    Thread.sleep(100)
    println(b.now)
  }

  {
    println("-- Timer --")
    val t = Timer(10.millis)
    var count = 0
    val o = t.trigger {
      count = count + 1
    }
    Thread.sleep(100)
    println(count)
    Thread.sleep(100)
    println(count)
    Thread.sleep(100)
    println(count)
  }

  {
    println("-- Delay --")
    val a = Var(10)
    val b = a.delay(250.millis)

    a() = 5
    println(b.now) // 10
    Thread.sleep(300)
    println(b.now) // 5
    a() = 4
    println(b.now) // 4 只有第一次delay？
    Thread.sleep(300)
    println(b.now) // 4
  }

  {
    println("-- Debounce --")
    val a = Var(10)
    // 每100毫秒内更新不会超过1次
    val b = a.debounce(100.millis)
    a() = 5
    println(b.now)

    a() = 2
    println(b.now)

    a() = 1
    println(b.now)
    Thread.sleep(101)
    println(b.now)
  }

  System.exit(0)
}
