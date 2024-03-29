package com.peknight.demo.frontend.apache.echarts

import com.peknight.demo.frontend.ecomfe.zrender.core.*
import com.peknight.demo.frontend.ecomfe.zrender.graphic.*
import org.scalajs.dom

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
  type ZRFontStyle = "normal" | "italic" | "oblique"
  type ZRFontWeight = "normal" | "bold" | "bolder" | "lighter" | Number

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

  // types.d.ts 335
  type DimensionDefinitionLoose = DimensionName | DimensionDefinition
  // types.d.ts 345
  type SeriesLayoutBy = "column" | "row"
  type OptionSourceHeader = Boolean | "auto" | Number
  type SeriesDataType = "main" | "node" | "edge"

  // types.d.ts 453
  type OptionId = String | Number
  type OptionName = String | Number

  // types.d.ts 445
  type OptionDataItem = OptionDataValue | js.Dictionary[OptionDataValue] | js.Array[OptionDataValue] | OptionDataItemObject[OptionDataValue]

  // types.d.ts 465
  type OptionDataValue = String | Number | js.Date
  type OptionDataValueNumeric = Number | "-"
  // types.d.ts 471
  type DisplayState = "normal" | "emphasis" | "blur" | "select"

  // types.d.ts 514
  type DecalDashArrayX = Number | js.Array[Number | js.Array[Number]]
  type DecalDashArrayY = Number | js.Array[Number]

  // types.d.ts 583
  type ColorBy = "series" | "data"

  // types.d.ts 657
  type SymbolSizeCallback[T] = js.Function2[js.Any, T, Number | js.Array[Number]]
  type SymbolCallback[T] = js.Function2[js.Any, T, String]
  type SymbolRotateCallback[T] = js.Function2[js.Any, T, Number]
  type SymbolOffsetCallback[T] = js.Function2[js.Any, T, String | Number | js.Array[String | Number]]

  // types.d.ts 781
  type LabelFormatterCallback[T] = js.Function1[T, String]

  // types.d.ts 836
  type LabelLayoutOptionCallback = js.Function1[LabelLayoutOptionCallbackParams, LabelLayoutOption]

  // types.d.ts 895
  type TooltipFormatterCallback[T] =
    // For async callback params will be an array on axis trigger.
    // (params, asyncTicket) => String | HTMLElement | Array[HTMLElement]
    js.Function2[T, String, String | dom.HTMLElement | js.Array[dom.HTMLElement]] |
      // For async callback. Returned html string will be a placeholder when callback is not invoked.
      // (params, asyncTicket, callback: (cbTicket, htmlOrDomNodes) => Unit) => String | HTMLElement | Array[HTMLElement]
      js.Function3[T, String, js.Function2[String, String | dom.HTMLElement | js.Array[dom.HTMLElement], Unit],
        String | dom.HTMLElement | js.Array[dom.HTMLElement]]

  // types.d.ts 907
  type TooltipBuiltinPosition = "inside" | "top" | "left" | "right" | "bottom"

  // types.d.ts 1021
  type SeriesTooltipOption = CommonTooltipOption[CallbackDataParams] & TriggerMixin

  // types.d.ts 1096
  type BlurScope = "coordinateSystem" | "series" | "global"

  // types.d.ts 1107
  type DefaultEmphasisFocus = "none" | "self" | "series"

  // types.d.ts 1262
  type SamplingFunc = js.Function1[js.Array[Number], Number]

  // model.d.ts 175
  type ModelFinderIndexQuery = Number | js.Array[Number] | "all" | "none" | false
  type ModelFinderIdQuery = OptionId | js.Array[OptionId]
  type ModelFinderNameQuery = OptionId | js.Array[OptionId]
  type ModelFinder = String | ModelFinderObject
