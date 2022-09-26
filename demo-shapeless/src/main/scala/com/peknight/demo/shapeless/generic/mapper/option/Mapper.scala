package com.peknight.demo.shapeless.generic.mapper.option

import cats.Id

trait Mapper[A, B] extends com.peknight.demo.shapeless.generic.mapper.Mapper[Option, A, B]
