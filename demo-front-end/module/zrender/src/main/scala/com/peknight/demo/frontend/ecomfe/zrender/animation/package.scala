package com.peknight.demo.frontend.ecomfe.zrender

import scala.scalajs.js

package object animation:

  // easing.ts 9
  type AnimationEasing = easingFunc
  // easing.ts 394
  type easingFunc = js.Function1[Number, Number]
