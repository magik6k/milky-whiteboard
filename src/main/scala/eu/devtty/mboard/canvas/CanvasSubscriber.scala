package eu.devtty.mboard.canvas

import eu.devtty.ipfs.IpfsNode
import eu.devtty.mboard.PubsubEvents
import eu.devtty.mboard.util.Buffer
import eu.devtty.multihash.MultiHash

import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

class CanvasSubscriber(ipfs: IpfsNode, chain: Chain, room: String, canvas: Canvas) {
  private val _id = ipfs.id()
  lazy val id = _id.value.get.get.id //TODO: That's baaaaaad
  val initializer = new ChainInitializer(ipfs, room, canvas)

  ipfs.pubsub.subscribe(room, { msg =>
    if(msg.from != id) {
      msg.data.readInt8(0) match {
        case PubsubEvents.DRAW_CHUNK =>
          ipfs.block.get(msg.data.slice(1)) andThen {
            case Success(block) =>
              canvas.drawImage(block.data.toString(), msg.from)
              chain.put(MultiHash.toB58String(msg.data.slice(1)))
          }
        case PubsubEvents.LIVE_PART =>
          val sx = msg.data.readDoubleLE(1)
          val sy = msg.data.readDoubleLE(9)
          val ex = msg.data.readDoubleLE(17)
          val ey = msg.data.readDoubleLE(25)

          canvas.drawPart(sx, sy, ex, ey, msg.from)
        case PubsubEvents.REQUEST_CHAIN =>
          if(chain.top != null) {
            println("Answer chain request")
            val msg = Buffer.alloc(chain.top.length + 1)
            msg.writeInt8(PubsubEvents.CHAIN, 0)
            msg.write(chain.top, 1)
            ipfs.pubsub.publish(room, msg)
          }
        case PubsubEvents.CHAIN =>
          initializer.registerBootCandidate(msg.data.slice(1).toString())
      }
    }
  }).andThen {
    case Success(_) => println(s"Subscribed to room $room")
  }

  js.Dynamic.global.peers = { () =>
    ipfs.pubsub.peers(room).andThen {
      case Success(list) => println("Peers:"); list.foreach(println)
    }
  }

  js.Dynamic.global.speers = { () =>
    ipfs.swarm.peers().andThen {
      case Success(list) => println("Peers:"); list.map(_.addr.toString()).foreach(println)
    }
  }
}
