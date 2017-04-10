package eu.devtty.mboard.canvas

import eu.devtty.ipfs.IpfsNode
import eu.devtty.mboard.{PubsubEvents, Status}
import eu.devtty.mboard.util.Buffer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.scalajs.js.timers.setTimeout
import scala.util.{Failure, Success}


class ChainInitializer(ipfs: IpfsNode, room: String, canvas: Canvas) {
  var bestBootCandidate: Block = _
  var initiated = false

  setTimeout(3000) {
    val msg = Buffer.alloc(1)
    msg.writeInt8(PubsubEvents.REQUEST_CHAIN, 0)
    ipfs.pubsub.publish(room, msg)
    Status.status("Seeking peers")
  }

  setTimeout(5000) {
    initiated = true
    init()
  }

  def registerBootCandidate(cid: String): Unit = {
    if(initiated)
      return

    println("Cn: " + cid)
    ipfs.dag.get(cid).map {_.value.asInstanceOf[Block]}.map { res =>
      println(s"Cdepth: ${res.depth}")
      if(bestBootCandidate == null || bestBootCandidate.depth < res.depth) {
        bestBootCandidate = res
      }
    }
  }

  def init(): Unit = {
    if(bestBootCandidate != null) {
      Status.status("Downloading chain")

      def parseChain(top: Block): Future[Vector[String]] = {
        println(s"Chain get at depth ${top.depth}")
        Status.status(s"Downloading block ${top.depth}")
        val nodes = top.nodes.toArray.toVector
        if(top.previous != null) {
          ipfs.dag.get(top.previous).map {_.value.asInstanceOf[Block]}.flatMap { previous =>
            parseChain(previous)
          }.map { _ ++ nodes }
        } else {
          Promise.successful(nodes).future
        }
      }

      parseChain(bestBootCandidate).map { blocks =>
        Future.sequence(blocks.map { hash =>
          ipfs.block.get(hash).map { block =>
            Status.status(s"Downloading part $hash")
            block
          }
        }).map(_.foreach(block => canvas.drawImage(block.data.toString(), null))).andThen {
          case Success(_) => Status.done()
          case Failure(e) => Status.status("Error; see dev console"); throw e
        }
      }
    } else {
      Status.done()
    }

  }
}
