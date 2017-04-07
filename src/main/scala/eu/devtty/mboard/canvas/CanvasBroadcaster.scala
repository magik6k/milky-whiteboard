package eu.devtty.mboard.canvas

import eu.devtty.ipfs.IpfsNode
import eu.devtty.mboard.PubsubEvents
import eu.devtty.mboard.util.Buffer
import eu.devtty.multihash.MultiHash

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.scalajs.js.timers._
import org.scalajs.dom.window

class CanvasBroadcaster(ipfs: IpfsNode, chain: Chain, room: String) {
  def commit(data: String): Unit = {
    ipfs.block.put(Buffer.from(data)).andThen {
      case Success(block) =>
        chain.put(MultiHash.toB58String(block.cid.buffer))

        val msg = Buffer.alloc(block.cid.buffer.length + 1)
        msg.writeInt8(PubsubEvents.DRAW_CHUNK, 0)
        block.cid.buffer.copy(msg, 1)

        ipfs.pubsub.publish(room, msg)
    }
  }

  var liveTimeout: SetTimeoutHandle = _
  var curStart: (Double, Double) = _
  var lastPush: Double = window.performance.now()

  def live(start: (Double, Double), end: (Double, Double)): Unit = {
    if(curStart == null)
      curStart = start

    if(liveTimeout != null) {
      clearTimeout(liveTimeout)
      if(window.performance.now() - lastPush >= 75.0) {
        sendLive(end)
        return
      }
    }

    liveTimeout = setTimeout(75) {
      sendLive(end)
    }
  }

  private def sendLive(end: (Double, Double)): Unit = {
    val msg = Buffer.alloc(8 * 4 + 1)
    msg.writeInt8(PubsubEvents.LIVE_PART, 0)

    msg.writeDoubleLE(curStart._1, 1)
    msg.writeDoubleLE(curStart._2, 9)
    msg.writeDoubleLE(end._1, 17)
    msg.writeDoubleLE(end._2, 25)
    ipfs.pubsub.publish(room, msg)

    curStart = null
    liveTimeout = null
    lastPush = window.performance.now()
  }
}
