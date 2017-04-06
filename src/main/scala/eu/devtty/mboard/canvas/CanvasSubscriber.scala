package eu.devtty.mboard.canvas

import eu.devtty.ipfs.IpfsNode

import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

class CanvasSubscriber(ipfs: IpfsNode, room: String) {
  private val _id = ipfs.id()
  lazy val id = _id.value.get.get.id //TODO: That's baaaaaad

  ipfs.pubsub.subscribe(room, { msg =>
    if(msg.from != id) {
      println("msg from " + msg.from)
    }
  }).andThen {
    case Success(_) => println(s"Subscribed to room $room")
  }

  js.Dynamic.global.peers = { () =>
    ipfs.pubsub.peers(room).andThen {
      case Success(list) => println("Peers:"); list.foreach(println)
    }
  }
}
