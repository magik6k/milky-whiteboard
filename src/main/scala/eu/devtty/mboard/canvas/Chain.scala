package eu.devtty.mboard.canvas

import eu.devtty.ipfs.{DagPutOptions, IpfsNode}
import eu.devtty.multihash.MultiHash
import io.scalajs.nodejs.setImmediate

import scala.collection.mutable
import scala.scalajs.js.JSConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.util.Success

class Chain(ipfs: IpfsNode, canvas: Canvas) {
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
    addBlock()
  }

  private def addBlock(): Unit = {
    val block = createBlock()
    depth += 1
    queue = mutable.Seq[String]()

    block.andThen {
      case Success(b) =>
      ipfs.dag.put(b, DagPutOptions("dag-cbor", "sha2-256")).map { cid =>
        top = cid.toBaseEncodedString()
        println(s"block ${b.depth}: $top on ${b.previous}")

        if (queue.nonEmpty)
          setImmediate { () => addBlock() }
        else
          queue = null
      }
    }
  }

  private def createBlock(): Future[Block] = {

    if(depth % 15 != 0 || depth == 0) {
      val blockPromise = Promise[Block]
      val block = js.Dynamic.literal(
        previous = top,
        nodes = queue.toJSArray,
        depth = depth
      ).asInstanceOf[Block]

      blockPromise.success(block)
      blockPromise.future
    } else {
      val presentQueue = queue.clone().toJSArray

      canvas.createImageBlock().map { imageBlock =>
        js.Dynamic.literal(
          previous = top,
          nodes = presentQueue.toJSArray,
          depth = depth,
          checkpoint = MultiHash.toB58String(imageBlock.cid.buffer)
        ).asInstanceOf[Block]
      }
    }

  }
}

@js.native
trait Block extends js.Object {
  val previous: String = js.native
  val nodes: js.Array[String] = js.native
  val depth: Int = js.native
  val checkpoint: js.UndefOr[String] = js.native //Optional hash of data of entire canvas
}
