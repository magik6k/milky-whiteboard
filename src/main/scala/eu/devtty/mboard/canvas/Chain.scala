package eu.devtty.mboard.canvas

import eu.devtty.ipfs.{DagPutOptions, IpfsNode}
import io.scalajs.nodejs.setImmediate

import scala.collection.mutable
import scala.scalajs.js.JSConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

class Chain(ipfs: IpfsNode) {
  var top: String = _
  var depth: Int = 0

  //Instance will exist when block is being added
  var queue: mutable.Seq[String] = _

  def put(dataLink: String): Unit = {
    if(queue != null) {
      queue :+= dataLink
      return
    }

    queue = mutable.Seq(dataLink)
    createBlock()
  }

  private def createBlock(): Unit = {
    val block = js.Dynamic.literal(
      previous = top,
      nodes = queue.toJSArray,
      depth = depth
    ).asInstanceOf[Block]
    depth += 1
    queue = mutable.Seq[String]()

    ipfs.dag.put(block, DagPutOptions("dag-cbor", "sha2-256")).map { cid =>
      top = cid.toBaseEncodedString()
      println(s"block ${block.depth}: $top on ${block.previous}")

      if(queue.nonEmpty)
        setImmediate { () => createBlock() }
      else
        queue = null
    }
  }
}

@js.native
trait Block extends js.Object {
  val previous: String = js.native
  val nodes: js.Array[String] = js.native
  val depth: Int = js.native
}
