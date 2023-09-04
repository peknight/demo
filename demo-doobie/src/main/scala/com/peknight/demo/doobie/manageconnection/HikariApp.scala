package com.peknight.demo.doobie.manageconnection

import cats.effect.*
import cats.implicits.*
import com.zaxxer.hikari.HikariConfig
import doobie.*
import doobie.hikari.*
import doobie.implicits.*

object HikariApp extends IOApp.Simple:

  // Resource yielding a transactor configured with a bounded connect EC and an unbounded
  // transaction EC. Everything will be closed and shut down cleanly after use.
  val transactor: Resource[IO, HikariTransactor[IO]] =
    for
      hikariConfig <- Resource.pure {
        val config = new HikariConfig()
        config.setDriverClassName("org.h2.Driver")
        config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
        config.setUsername("sa")
        config.setPassword("")
        config
      }
      xa <- HikariTransactor.fromHikariConfig[IO](hikariConfig)
    yield xa
    // 旧版逻辑
    // for
    //   ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
    //   xa <- HikariTransactor.newHikariTransactor[IO](
    //     "org.h2.Driver",                        // driver classname
    //     "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",   // connect URL
    //     "sa",                                   // username
    //     "",                                     // password
    //     ce                                      // await connection here
    //   )
    // yield xa
  
  //noinspection DuplicatedCode
  val run = transactor.use { xa =>
    // Construct and run your server here!
    for
      n <- sql"select 42".query[Int].unique.transact(xa)
      _ <- IO.println(n)
    yield ()
  }

