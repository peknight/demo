package com.peknight.demo.shapeless.generic.mapper.option

trait BiMapper[A, B] extends com.peknight.demo.shapeless.generic.mapper.BiMapper[Option, A, B], Mapper[A, B]
