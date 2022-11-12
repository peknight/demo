package com.peknight.demo.frontend.ecomfe.zrender

import org.scalajs.dom.{HTMLCanvasElement, HTMLImageElement, HTMLVideoElement}

package object core:
  type ImageLike = HTMLImageElement | HTMLCanvasElement | HTMLVideoElement
  type TextAlign = "left" | "center" | "right"
  type TextVerticalAlign = "top" | "middle" | "bottom"
  type BuiltinTextPosition = "left" | "right" | "top" | "bottom" | "inside" | "insideLeft" | "insideRight"| "insideTop" |
  "insideBottom" | "insideTopLeft" | "insideTopRight"| "insideBottomLeft"| "insideBottomRight"