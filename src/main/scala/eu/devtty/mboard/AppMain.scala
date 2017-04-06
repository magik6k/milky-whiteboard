package eu.devtty.mboard

import eu.devtty.ipfs.IpfsNode

import scala.scalajs.js

class AppMain(ipfs: IpfsNode) {
  println("IPFS node running")

  js.Dynamic.global.ipfs = ipfs.asInstanceOf[js.Any]
}
