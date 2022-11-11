package com.peknight.demo.frontend.apache.echarts

import com.peknight.demo.frontend.ecomfe.zrender.core.*
import com.peknight.demo.frontend.ecomfe.zrender.graphic.*

import scala.scalajs.js

package object util:

  // format.d.ts 37
  type TooltipMarker = String | RichTextTooltipMarker

  // types.d.ts 33
  type RendererType = "canvas" | "svg"
  type LayoutOrient = "vertical" | "horizontal"
  type HorizontalAlign = "left" | "center" | "right"
  type VerticalAlign = "top" | "middle" | "bottom"
  type ColorString = String
  type ZRColor = ColorString | LinearGradientObject | RadialGradientObject | PatternObject
  type ZRLineType = "solid" | "dotted" | "dashed" | Number | js.Array[Number]

  // types.d.ts 44
  type ZRTextAlign = TextAlign
  type ZRTextVerticalAlign = TextVerticalAlign

  // types.d.ts 249
  type TooltipRenderMode = "html" | "richText"
  type TooltipOrderMode = "valueAsc" | "valueDesc" | "seriesAsc" | "seriesDesc"
  type OrdinalRawValue = String | Number
  type OrdinalNumber = Number

  // types.d.ts 274
  type ParsedValueNumeric = Number | OrdinalNumber
  type ScaleDataValue = ParsedValueNumeric | OrdinalRawValue | js.Date

  // types.d.ts 318
  type DimensionName = String
  // types.d.ts 347
  type SeriesDataType = "main" | "node" | "edge"

  // types.d.ts 453
  type OptionId = String | Number
  type OptionName = String | Number

  // types.d.ts 445
  type OptionDataItem = OptionDataValue | js.Dictionary[OptionDataValue] | js.Array[OptionDataValue] | OptionDataItemObject[OptionDataValue]

  // types.d.ts.465
  type OptionDataValue = String | Number | js.Date
  // types.d.ts.471
  type DisplayState = "normal" | "emphasis" | "blur" | "select"

  // types.d.ts 583
  type ColorBy = "series" | "data"

  // types.d.ts 836
  type LabelLayoutOptionCallback = js.Function1[LabelLayoutOptionCallbackParams, LabelLayoutOption]

  // types.d.ts 907
  type TooltipBuiltinPosition = "inside" | "top" | "left" | "right" | "bottom"

  // types.d.ts 1021
  type SeriesTooltipOption = CommonTooltipOption[CallbackDataParams] & TriggerMixin
