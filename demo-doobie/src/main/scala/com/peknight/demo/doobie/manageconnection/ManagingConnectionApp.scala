package com.peknight.demo.doobie.manageconnection

import cats.effect.*
import cats.implicits.*
import com.peknight.demo.doobie.xa
import doobie.*
import doobie.free.connection.unit
import doobie.hikari.*
import doobie.implicits.*
import doobie.util.transactor.Strategy

import java.sql.Connection
import javax.sql.DataSource

object ManagingConnectionApp:
  // Resource yielding a DataSourceTransactor[IO] wrapping the given `DataSource`
  def transactor(ds: DataSource): Resource[IO, DataSourceTransactor[IO]] =
    for
      ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
    yield Transactor.fromDataSource[IO](ds, ce)

  // A Transactor[IO] wrapping the given `Connection`
  def transactor(c: Connection): Transactor[IO] = Transactor.fromConnection[IO](c)

  // 定制transactor，如测试时的sql都要回滚，那么可以这样设置
  val testXa = Transactor.after.set(xa, HC.rollback)

  // 对于hive的jdbc不支持提交和回滚的情况，可以这样定制来兼容
  val hiveXa = Transactor.strategy.set(xa, Strategy.default.copy(after = unit, oops = unit))