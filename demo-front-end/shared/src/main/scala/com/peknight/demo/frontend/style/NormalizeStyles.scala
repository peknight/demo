package com.peknight.demo.frontend.style

import scalacss.ProdDefaults.*
import scalacss.internal.Attr
import scalacss.internal.Dsl.*

/**
 * normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css
 * https://necolas.github.io/normalize.css/8.0.1/normalize.css
 */
object NormalizeStyles extends StyleSheet.Standalone:
  import dsl.*

  /* Document */

  /**
   * 1. Correct the line height in all browsers.
   * 2. Prevent adjustments of font size after orientation changes in iOS.
   */
  "html" - (
    // 1
    lineHeight(1.15),
    // 2
    Attr.real("-webkit-text-size-adjust") := "100%"
  )

  /* Sections */

  /**
   * Remove the margin in all browsers.
   */
  "body" - margin.`0`

  /**
   * Render the `main` element consistently in IE
   */
  "main" - display.block

  /**
   * Correct the font size and margin on `h1` elements within `section` and
   * `article` contexts in Chrome, Firefox, and Safari.
   */
  "h1" - (fontSize(2.em), margin(0.67.em, `0`))

  /* Grouping content*/

  /**
   * 1. Add the correct box sizing in Firefox.
   * 2. Show the overflow in Edge and IE.
   */
  "hr" - (
    // 1
    boxSizing.contentBox,
    // 1
    height.`0`,
    // 2
    overflow.visible
  )

  /**
   * 1. Correct the inheritance and scaling of font size in all browsers.
   * 2. Correct the odd `em` font sizing in all browsers.
   */
  "pre" - (
    // 1
    fontFamily :=! "monospace, monospace",
    // 2
    fontSize(1.em)
  )

  /* Text-level semantics */

  /**
   * Remove the gray background on active links in IE 10.
   */
  "a" - backgroundColor(transparent)

  /**
   * 1. Remove the bottom border in Chrome 57-
   * 2. Add the correct text decoration in Chrome, Edge, IE, Opera, and Safari.
   */
  "abbr".attrExists("title") - (
    // 1
    borderBottom.none,
    // 2
    textDecoration := "underline",
    // 2
    textDecoration := "underline dotted",
  )

  /**
   * Add the correct font weight in Chrome, Edge, and Safari.
   */
  "b, string" - fontWeight.bolder

  /**
   * 1. Correct the inheritance and scaling of font size in all browsers.
   * 2. Correct the odd `em` font sizing in all browsers.
   */
  "code, kbd, samp" - (
    // 1
    fontFamily :=! "monospace, monospace",
    // 2
    fontSize(1.em)
  )

  /**
   * Add the correct font size in all browsers.
   */
  "small" - fontSize(80.%%)

  /**
   * Prevent `sub` and `sup` elements from affecting the line height in
   * all browsers.
   */
  "sub, sup" - (
    fontSize(75.%%),
    lineHeight.`0`,
    position.relative,
    verticalAlign.baseline,
  )

  "sub" - bottom(-0.25.em)

  "sup" - top(-0.5.em)

  /* Embedded content */

  /**
   * Remove the border on images inside links in IE 10.
   */
  "img" - borderStyle.none

  /* Forms */

  /**
   * 1. Change the font styles in all browsers.
   * 2. Remove the margin in Firefox and Safari.
   */
  "button, input, optgroup, select, textarea" - (
    // 1
    fontFamily.inherit,
    // 1
    fontSize(100.%%),
    // 1
    lineHeight(1.15),
    // 2
    margin.`0`
  )

  /**
   * Show the overflow in IE.
   * 1. Show the overflow in Edge.
   */
  "button, input" - overflow.visible

  /**
   * Remove the inheritance of text transform in Edge, Firefox, and IE.
   * 1. Remove the inheritance of text transform in Firefox.
   */
  "button, select" - textTransform.none

  /**
   * Correct the inability to style clickable types in iOS and safari.
   */
  "button, [type=\"button\"], [type=\"reset\"], [type=\"submit\"]" - (
    Attr.real("-webkit-appearance") := "button"
  )

  /**
   * Remove the inner border and padding in Firefox.
   */
  "button::-moz-focus-inner," +
    "[type=\"button\"]::-moz-focus-inner," +
    "[type=\"reset\"]::-moz-focus-inner," +
    "[type=\"submit\"]::-moz-focus-inner" - (
    borderStyle.none,
    padding.`0`,
  )

  /**
   * Restore the focus styles unset by the previous rule.
   */
  "button::-moz-focusring," +
    "[type=\"button\"]::-moz-focusring," +
    "[type=\"reset\"]::-moz-focusring," +
    "[type=\"submit\"]::-moz-focusring" - (
    outline :=! "1px dotted ButtonText"
  )

  /**
   * Correct the padding in Firefox.
   */
  "fieldset" - padding(0.35.em, 0.75.em, 0.625.em)

  /**
   * 1. Correct the text wrapping in Edge and IE.
   * 2. Correct the color inheritance from `fieldset` elements in IE.
   * 3. Remove the padding so developers are not caught out when they zero out
   *   `fieldset` elements in all browsers.
   */
  "legend" - (
    // 1
    boxSizing.borderBox,
    // 2
    color.inherit,
    // 1
    display.table,
    // 1
    maxWidth(100.%%),
    // 3
    padding.`0`,
    // 1
    whiteSpace.normal,
  )

  /**
   * Add the correct vertical alignment in Chrome, Firefox, and Opera.
   */
  "progress" - verticalAlign.baseline

  /**
   * Remove the default vertical scrollbar in IE 10+.
   */
  "textarea" - overflow.auto

  /**
   * 1. Add the correct box sizing in IE 10.
   * 2. Remove the padding in IE 10.
   */
  "[type=\"checkbox\"], [type=\"radio\"]" - (
    // 1
    boxSizing.borderBox,
    // 2
    padding.`0`
  )

  /**
   * Correct the cursor style of increment and decrement buttons in Chrome.
   */
  "[type=\"number\"]::-webkit-inner-spin-button, [type=\"number\"]::-webkit-outer-spin-button" - height.auto

  /**
   * 1. Correct the odd appearance in Chrome and Safari.
   * 2. Correct the outline style in Safari.
   */
  "".attr("type", "search") - (
    // 1
    Attr.real("-webkit-appearance") := "textfield",
    // 2
    outlineOffset(-2.px)
  )

  /**
   * Remove the inner padding in Chrome and Safari on macOS.
   */
  "[type=\"search\"]::-webkit-search-decoration" - style(Attr.real("-webkit-appearance") := "none")

  /**
   * 1. Correct the inability to style clickable types in iOS and Safari.
   * 2. Change font properties to `inherit` in Safari.
   */
  "::-webkit-file-upload-button" - (
    // 1
    Attr.real("-webkit-appearance") := "button",
    // 2
    font := "inherit"
  )

  /* Interactive */

  /**
   * Add the correct display in Edge, IE 10+, and Firefox.
   */
  "details" - display.block

  /**
   * Add the correct display in all browsers.
   */
  "summary" - display.listItem

  /* Misc */

  /**
   * Add the correct display in IE 10+.
   */
  "template" - display.none

  /**
   * Add the correct display in IE 10.
   */
  "".attrExists("hidden") - display.none
