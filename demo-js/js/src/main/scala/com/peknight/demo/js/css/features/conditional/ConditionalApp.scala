package com.peknight.demo.js.css.features.conditional

import scalacss.DevDefaults.*

object ConditionalApp extends StyleSheet.Inline:

  import dsl.*

  // Pseudo Selectors

  &.hover                             // :hover
  &.hover.visited                     // :hover:visited
  &.hover.not(_.visited)              // :hover:not(:visited)
  &.hover.not(_.firstChild.visited)   // :hover:not(:first-child:visited)
  &.nthChild(3).not(".debug")         // :nth-child(3):not(.debug)
  &.nthChild("3n+2")                  // :nth-child(3n+2)
  &.attr("custom-attr", "bla").hover  // [custom-attr="bla"]:hover
  &.attrExists("custom-attr").hover   // [custom-attr]:hover

  // Media Queries

  // Creating a single query
  // media.<attr1>
  // media.<attr1>.<attr2>…<attrn>

  // Composing queries
  // <query1> & <query2> [… & <queryn>]

  // Single queries:
  media.color                         // @media (color)
  media.screen                        // @media screen
  media.screen.landscape.color        // @media screen and (orientation:landscape) and (color)
  media.not.handheld.minWidth(320.px) // @media not handheld and (min-width:320px)

  // Multiple queries:

  media.screen & media.not.handheld
  // produces: @media screen, not handheld

  media.screen.color & media.not.handheld & media.minHeight(240.px)
  // produces: @media screen and (color), not handheld, (min-height:240px)

  // These are all equivalent. All produce:
  //   @media screen { <css>:active {...} }
  // To add a PS to a MQ, specify the PS as a method on the MQ
  media.screen.active
  // To add a PS to a MQ: If you have an existing PS, use &.
  media.screen & &.active
  // To add a MQ to a PS, use &.
  &.active & media.screen