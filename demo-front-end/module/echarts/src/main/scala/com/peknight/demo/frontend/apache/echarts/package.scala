package com.peknight.demo.frontend.apache

import org.scalajs.dom.*

import scala.scalajs.js

package object echarts:
  type Number = Double | Int

  type OptionId = String | Number
  type OptionName = String | Number

  // line 57
  type ImageLike = HTMLImageElement | HTMLCanvasElement | HTMLVideoElement
  type TextVerticalAlign = "top" | "middle" | "bottom"
  type TextAlign = "left" | "center" | "right"
  type FontWeight = "normal" | "bold" | "bolder" | "lighter" | Number
  type FontStyle = "normal" | "italic" | "oblique"
  type BuiltinTextPosition = "left" | "right" | "top" | "bottom" | "inside" | "insideLeft" | "insideRight" |
    "insideTop" | "insideBottom" | "insideTopLeft" | "insideTopRight" | "insideBottomLeft" | "insideBottomRight"
  type ZRRawMouseEvent = MouseEvent & ZREventProperties
  type ZRRawTouchEvent = TouchEvent & ZREventProperties
  type ZRRawPointerEvent = TouchEvent & ZREventProperties
  type ZRRawEvent = ZRRawMouseEvent | ZRRawTouchEvent | ZRRawPointerEvent
  type ElementEventName = "click" | "dblclick" | "mousewheel" | "mouseout" | "mouseover" | "mouseup" | "mousedown" |
    "mousemove" | "contextmenu" | "drag" | "dragstart" | "dragend" | "dragenter" | "dragleave" | "dragover" | "drop" |
    "globalout"
  type ElementEventNameWithOn = "onclick" | "ondblclick" | "onmousewheel" | "onmouseout" | "onmouseup" | "onmousedown" |
    "onmousemove" | "oncontextmenu" | "ondrag" | "ondragstart" | "ondragend" | "ondragenter" | "ondragleave" |
    "ondragover" | "ondrop"

  // line 117
  type PatternObject = ImagePatternObject | SVGPatternObject;

  // line 394
  type easingFunc = js.Function1[Number, Number]
  // TODO
  type AnimationEasing = easingFunc

  // line 2355
  // TODO
  type OptionAxisType = "value" | "category" | "time" | "log"

  // line 2542
  type AxisBaseOption = ValueAxisBaseOption | LogAxisBaseOption | CategoryAxisBaseOption | TimeAxisBaseOption | AxisBaseOptionCommon

  // 2933
  type CartesianAxisPosition = "top" | "bottom" | "left" | "right"
  type CartesianAxisOption = AxisBaseOption & CartesianAxisMixin
  type XAXisOption = CartesianAxisOption & XAXisMainTypeMixin
  type YAXisOption = CartesianAxisOption & YAXisMainTypeMixin

  // line 3191
  type SelectorType = "all" | "inverse"

  // line 5882
  type TooltipMarker = String | RichTextTooltipMarker
  type TooltipMarkerType = "item" | "subItem"

  // line 5941
  type TopLevelFormatterParams = CallbackDataParams | js.Array[CallbackDataParams]

  // line 6081
  type RendererType = "canvas" | "svg"
  type LayoutOrient = "vertical" | "horizontal"
  type HorizontalAlign = "left" | "center" | "right"
  type VerticalAlign = "top" | "middle" | "bottom"
  type ColorString = String
  type ZRColor = ColorString | LinearGradientObject | RadialGradientObject | PatternObject
  type ZRLineType = "solid" | "dotted" | "dashed" | Number | js.Array[Number]
  type ZRFontStyle = "normal" | "italic" | "oblique"
  type ZRFontWeight = "normal" | "bold" | "bolder" | "lighter" | Number
  type ZREasing = AnimationEasing
  type ZRTextAlign = TextAlign
  type ZRTextVerticalAlign = TextVerticalAlign
  type ZRRectLike = RectLike
  type ZRStyleProps = PathStyleProps | ImageStyleProps | TSpanStyleProps | TextStyleProps
  type ZRElementEventName = ElementEventName | "globalout"
  type ComponentFullType = String

  // line 6232
  type TooltipRenderMode = "html" | "richText"
  type TooltipOrderMode = "valueAsc" | "valueDesc" | "seriesAsc" | "seriesDesc"
  type OrdinalRawValue = String | Number
  type OrdinalNumber = Number

  // line 6256
  type ParsedValue = ParsedValueNumeric | OrdinalRawValue
  type ParsedValueNumeric = Number | OrdinalNumber
  type ScaleDataValue = ParsedValueNumeric | OrdinalRawValue | js.Date

  // line 6270
  type DimensionIndex = Number
  type DimensionIndexLoose = DimensionIndex | String
  type DimensionName = String
  type DimensionLoose = DimensionName | DimensionIndexLoose

  // line 6299
  type OptionSourceHeader = Boolean | "auto" | Number
  type SeriesDataType = "main" | "node" | "edge"

  // line 6398
  type OptionDataItem = OptionDataValue | js.Dictionary[OptionDataValue] | js.Array[OptionDataValue] | OptionDataItemObject[OptionDataValue]

  // line 6418
  type OptionDataValue = String | Number | js.Date
  type OptionDataValueNumeric = Number | "-";
  type OptionDataValueDate = js.Date | String | Number
  type ModelOption = js.Any
  type ThemeOption = js.Dictionary[js.Any];
  type DisplayState = "normal" | "emphasis" | "blur" | "select"

  // line 6834
  type TooltipBuiltinPosition = "inside" | "top" | "left" | "right" | "bottom"

  // 6948
  type SeriesTooltipOption = CommonTooltipOption[CallbackDataParams] & TriggerMixin

  // line 10788
  type LegendComponentOption = LegendOption | ScrollableLegendOption

  // line 10796
  type LineSeriesOption$1 = LineSeriesOption & SeriesInjectedOption;
  type BarSeriesOption$1 = BarSeriesOption & SeriesInjectedOption;
  type ScatterSeriesOption$1 = ScatterSeriesOption & SeriesInjectedOption;
  type PieSeriesOption$1 = PieSeriesOption & SeriesInjectedOption;
  type RadarSeriesOption$1 = RadarSeriesOption & SeriesInjectedOption;
  type MapSeriesOption$1 = MapSeriesOption & SeriesInjectedOption;
  type TreeSeriesOption$1 = TreeSeriesOption & SeriesInjectedOption;
  type TreemapSeriesOption$1 = TreemapSeriesOption & SeriesInjectedOption;
  type GraphSeriesOption$1 = GraphSeriesOption & SeriesInjectedOption;
  type GaugeSeriesOption$1 = GaugeSeriesOption & SeriesInjectedOption;
  type FunnelSeriesOption$1 = FunnelSeriesOption & SeriesInjectedOption;
  type ParallelSeriesOption$1 = ParallelSeriesOption & SeriesInjectedOption;
  type SankeySeriesOption$1 = SankeySeriesOption & SeriesInjectedOption;
  type BoxplotSeriesOption$1 = BoxplotSeriesOption & SeriesInjectedOption;
  type CandlestickSeriesOption$1 = CandlestickSeriesOption & SeriesInjectedOption;
  type EffectScatterSeriesOption$1 = EffectScatterSeriesOption & SeriesInjectedOption;
  type LinesSeriesOption$1 = LinesSeriesOption & SeriesInjectedOption;
  type HeatmapSeriesOption$1 = HeatmapSeriesOption & SeriesInjectedOption;
  type PictorialBarSeriesOption$1 = PictorialBarSeriesOption & SeriesInjectedOption;
  type ThemeRiverSeriesOption$1 = ThemeRiverSeriesOption & SeriesInjectedOption;
  type SunburstSeriesOption$1 = SunburstSeriesOption & SeriesInjectedOption;
  type CustomSeriesOption$1 = CustomSeriesOption & SeriesInjectedOption;

  type SeriesOption$1 = LineSeriesOption$1 | BarSeriesOption$1 | ScatterSeriesOption$1 | PieSeriesOption$1 |
    RadarSeriesOption$1 | MapSeriesOption$1 | TreeSeriesOption$1 | TreemapSeriesOption$1 | GraphSeriesOption$1 |
    GaugeSeriesOption$1 | FunnelSeriesOption$1 | ParallelSeriesOption$1 | SankeySeriesOption$1 | BoxplotSeriesOption$1 |
    CandlestickSeriesOption$1 | EffectScatterSeriesOption$1 | LinesSeriesOption$1 | HeatmapSeriesOption$1 |
    PictorialBarSeriesOption$1 | ThemeRiverSeriesOption$1 | SunburstSeriesOption$1 | CustomSeriesOption$1
