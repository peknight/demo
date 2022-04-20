package com.peknight.demo.cats.introduction

trait JsonWriter[A]:
  def write(value: A): Json
