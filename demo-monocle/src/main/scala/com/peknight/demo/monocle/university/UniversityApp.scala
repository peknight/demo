package com.peknight.demo.monocle.university

import monocle.{Focus, Traversal}

object UniversityApp extends App:
  val uni = University("oxford", Map(
    "Computer Science" -> Department(45, List(
      Lecturer("john", "doe", 10),
      Lecturer("robert", "johnson", 16)
    )),
    "History" -> Department(30, List(
      Lecturer("arnold", "stones", 20)
    ))
  ))
  val departments = Focus[University](_.departments)
  println(departments.at("History").replace(None)(uni))

  val physics = Department(36, List(
    Lecturer("daniel", "jones", 12),
    Lecturer("roger", "smith", 14)
  ))
  println(departments.at("Physics").replace(Some(physics))(uni))

  val lecturers = Focus[Department](_.lecturers)
  val salary = Focus[Lecturer](_.salary)
  // 牛逼，刚想这个问题 就有这个解决方案↓
  val allLecturers: Traversal[University, Lecturer] = departments.each.andThen(lecturers).each
  println(allLecturers.andThen(salary).modify(_ + 2)(uni))

  val firstName = Focus[Lecturer](_.firstName)
  val lastName = Focus[Lecturer](_.lastName)

  val upperCasedFirstName = allLecturers.andThen(firstName).index(0).modify(_.toUpper)(uni)
  println(allLecturers.andThen(lastName).index(0).modify(_.toUpper)(upperCasedFirstName))
  // ↑ 冗余了，解决方案 ↓
  val firstAndLastNames = Traversal.apply2[Lecturer, String](_.firstName, _.lastName){ case (fn, ln, l) =>
    l.copy(firstName = fn, lastName = ln)
  }
  println(allLecturers.andThen(firstAndLastNames).index(0).modify(_.toUpper)(uni))



