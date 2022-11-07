package com.peknight.demo.frontend.channg.easylazyload

import org.querki.jquery.*
import scalatags.JsDom.all.*
import org.scalajs.dom

case class LazyLoadOptions(coverColor: String = "#dfdfdf", coverDiv: Frag = frag(), showTime: Int = 300,
                           offsetBottom: Int = 0, offsetTopm: Int = 50,
                           onLoadBackEnd: (Int, JQuery) => Unit = (_, _) => (),
                           onLoadBackStart: (Int, JQuery) => Unit = (_, _) => ())
