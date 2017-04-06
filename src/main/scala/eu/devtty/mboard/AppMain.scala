package eu.devtty.mboard

import java.util.UUID

import eu.devtty.ipfs.IpfsNode
import eu.devtty.mboard.canvas.Canvas
import org.scalajs.dom.window

import scala.scalajs.js
import scala.util.Try

class AppMain(ipfs: IpfsNode) {
  println("IPFS node running")

  js.Dynamic.global.ipfs = ipfs.asInstanceOf[js.Any]

  if(Try(UUID.fromString(window.location.hash.substring(1))).isFailure) {
    window.location.hash = UUID.randomUUID().toString
  }

  new Canvas("main", ipfs, window.location.hash)
}

