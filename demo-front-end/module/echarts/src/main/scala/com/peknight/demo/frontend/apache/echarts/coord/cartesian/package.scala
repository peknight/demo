package com.peknight.demo.frontend.apache.echarts.coord

package object cartesian:

  // AxisModel.d.ts
  type CartesianAxisPosition = "top" | "bottom" | "left" | "right"
  type CartesianAxisOption = AxisBaseOption & CartesianAxisMixin
  type XAXisOption = CartesianAxisOption & XAXisMainTypeMixin
  type YAXisOption = CartesianAxisOption & YAXisMainTypeMixin
