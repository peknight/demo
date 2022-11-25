package com.peknight.demo.frontend.vue.runtimecore

import scala.scalajs.js

// TODO
trait Component extends js.Object
object Component:
  def apply(
             data: js.UndefOr[js.ThisFunction1[CreateComponentPublicInstance, CreateComponentPublicInstance, js.Any]] = js.undefined,
             methods: js.UndefOr[MethodOptions] = js.undefined,
             beforeCreate: js.UndefOr[js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit]] = js.undefined,
             created: js.UndefOr[js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit]] = js.undefined,
             beforeMount: js.UndefOr[js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit]] = js.undefined,
             mounted: js.UndefOr[js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit]] = js.undefined,
             beforeUpdate: js.UndefOr[js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit]] = js.undefined,
             updated: js.UndefOr[js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit]] = js.undefined,
             activated: js.UndefOr[js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit]] = js.undefined,
             deactivated: js.UndefOr[js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit]] = js.undefined,
             beforeUnmount: js.UndefOr[js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit]] = js.undefined,
             unmounted: js.UndefOr[js.Function0[Unit] | js.ThisFunction0[js.Dynamic, Unit]] = js.undefined,
             setup: js.UndefOr[js.ThisFunction2[Unit, js.Dynamic, SetupContext[EmitsOptions], js.Promise[js.Dynamic] | js.Dynamic | js.Function0[VNodeChild] | Unit]] = js.undefined
           ): Component =
    val obj = new js.Object().asInstanceOf[js.Dynamic]
    data.foreach(obj.data = _)
    methods.foreach(obj.methods = _)
    beforeCreate.foreach(obj.beforeCreate = _)
    created.foreach(obj.created = _)
    beforeMount.foreach(obj.beforeMount = _)
    mounted.foreach(obj.mounted = _)
    beforeUpdate.foreach(obj.beforeUpdate = _)
    updated.foreach(obj.updated = _)
    activated.foreach(obj.activated = _)
    deactivated.foreach(obj.deactivated = _)
    beforeUnmount.foreach(obj.beforeUnmount = _)
    unmounted.foreach(obj.unmounted = _)
    setup.foreach(obj.setup = _)
    obj.asInstanceOf[Component]
