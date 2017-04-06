package eu.devtty.mboard.canvas

import eu.devtty.ipfs.IpfsNode
import eu.devtty.mboard.PubsubEvents
import eu.devtty.mboard.util.Buffer
import eu.devtty.multihash.MultiHash

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

class CanvasBroadcaster(ipfs: IpfsNode, room: String) {
  def commit(data: String): Unit = {
    ipfs.block.put(Buffer.from(data)).andThen {
      case Success(block) =>
        println("commit:" + MultiHash.toB58String(block.cid.buffer))
        val msg = Buffer.alloc(data.length + 1)
        msg.writeInt8(PubsubEvents.DRAW_CHUNK, 0)
        msg.write(data, 1)

        ipfs.pubsub.publish(room, msg)
    }
  }
}
