package com.peknight.demo.oauth2.page

import scalacss.internal.Dsl.c
import scalatags.generic.Bundle
import scalatags.text.Builder

class ProtectedResourcePage[Builder, Output <: FragT, FragT](override val bundle: Bundle[Builder, Output, FragT])
  extends OAuthPage(bundle):
  import bundle.all.*

  val index: Frag =
    super.jumbotron("Protected Resource", "success", c"#232")(
      "To access the resource, send a ", code("POST"), " request to ",
      code("http://localhost:8002/resource"), " and include a valid OAuth token."
    )

end ProtectedResourcePage

object ProtectedResourcePage:
  object Text extends ProtectedResourcePage(scalatags.Text)
end ProtectedResourcePage

