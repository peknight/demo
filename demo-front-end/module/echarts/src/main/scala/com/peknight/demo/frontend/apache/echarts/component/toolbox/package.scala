package com.peknight.demo.frontend.apache.echarts.component

import com.peknight.demo.frontend.apache.echarts.util.{CallbackDataParams, ItemStyleOption}

package object toolbox:
  type IconStyle = ItemStyleOption[CallbackDataParams] & IconTextStyleMixin
