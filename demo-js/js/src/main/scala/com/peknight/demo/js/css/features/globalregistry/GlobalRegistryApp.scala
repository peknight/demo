package com.peknight.demo.js.css.features.globalregistry

import scalacss.internal.mutable.GlobalRegistry

object GlobalRegistryApp:
  def onStartup(): Unit = GlobalRegistry.register(new BoxStyles)

  GlobalRegistry.onRegistration { s =>
    val styleCount = s.styles.size
    println(s"Registered $styleCount styles.")
  }

  GlobalRegistry.onRegistrationN { ss =>
    val sheetCount = ss.size
    val styleCount = ss.map(_.styles.size).sum
    println(s"Registered $sheetCount sheets with a total of $styleCount styles.")
  }

  val boxStyles = GlobalRegistry[BoxStyles].get
  // <.div(boxStyles.mainBox, "Yay")

