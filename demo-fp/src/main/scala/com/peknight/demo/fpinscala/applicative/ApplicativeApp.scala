package com.peknight.demo.fpinscala.applicative

import com.peknight.demo.fpinscala.monads.Monad.optionMonad

object ApplicativeApp extends App {
  val depts: Map[String, String] = Map("Alice" -> "Tech")
  val salaries: Map[String, Double] = Map("Alice" -> 100.0)
  val aliceDeptSalary: Option[String] = optionMonad.map2(depts.get("Alice"), salaries.get("Alice"))((dept, salary) =>
    s"Alice in $dept makes $salary per year"
  )

  val idsByName: Map[String, Int] = Map("Bob" -> 123)
  val deptsById: Map[Int, String] = Map(123 -> "HR")
  val salariesById: Map[Int, Double] = Map(123 -> 50.0)
  val bobDeptSalary: Option[String] = idsByName.get("Bob").flatMap(id =>
    optionMonad.map2(deptsById.get(id), salariesById.get(id))((dept, salary) => s"Bob in $dept makes $salary per year")
  )
}
