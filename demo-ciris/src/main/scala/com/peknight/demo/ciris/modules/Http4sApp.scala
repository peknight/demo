package com.peknight.demo.ciris.modules

import ciris.ConfigDecoder
import ciris.http4s.*
import org.http4s.Uri

object Http4sApp:

  val uriConfigDecoder: ConfigDecoder[String, Uri] = ConfigDecoder[String, Uri]

