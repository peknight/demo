package com.peknight.demo.js.lihaoyi.upickle.customization

object OptionPickler extends upickle.AttributeTagged:

  override given OptionWriter[T: Writer]: Writer[Option[T]] = implicitly[Writer[T]].comap[Option[T]] {
    case None => null.asInstanceOf[T]
    case Some(x) => x
  }
  override given OptionReader[T: Reader]: Reader[Option[T]] =
    new Reader.Delegate[Any, Option[T]](summon[Reader[T]].map(Some(_))):
      override def visitNull(index: Int) = None


