package com.peknight.demo.ciris.modules

// import io.circe.Decoder

case class SerialNumber(value: String)

// object SerialNumber:
//   // 用这个Decoder的话 直接给"abc"这样的配置就可以，如果使用generic.auto 那么就用{"value":"abc"}，yaml用value: abc
//   given Decoder[SerialNumber] = Decoder[String].map(apply)
