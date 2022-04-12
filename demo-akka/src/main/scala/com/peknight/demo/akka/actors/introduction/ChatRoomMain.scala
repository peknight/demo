package com.peknight.demo.akka.actors.introduction

import akka.NotUsed
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{Behavior, Terminated}

object ChatRoomMain {
    def apply(): Behavior[NotUsed] =
      Behaviors.setup { context =>
        val chatRoom = context.spawn(ChatRoom(), "chatroom")
        val gabblerRef = context.spawn(Gabbler(), "gabbler")
        context.watch(gabblerRef)
        chatRoom ! ChatRoom.GetSession("ol’ Gabbler", gabblerRef)

        Behaviors.receiveSignal {
          case (context, Terminated(actorRef)) =>
            context.log.info("{}", actorRef.path)
            Behaviors.stopped
        }
      }

//  def main(args: Array[String]): Unit = {
//    ActorSystem(ChatRoomMain(), "ChatRoomDemo")
//  }
}
