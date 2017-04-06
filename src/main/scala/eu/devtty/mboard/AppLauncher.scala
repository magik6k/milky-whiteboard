package eu.devtty.mboard

import eu.devtty.ipfs.jsnode.JsIpfs
import eu.devtty.multiaddr.Multiaddr

import scala.scalajs.js.JSApp
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object AppLauncher extends JSApp {
  def main(): Unit = {
    println("-mboard-")
    val ipfs = new JsIpfs(js.Dynamic.literal(
      EXPERIMENTAL = js.Dynamic.literal(
        pubsub = true
      ),
      Discovery = js.Dynamic.literal(
        MDNS = js.Dynamic.literal(
          Enabled = false
        ),
        webRTCStar = js.Dynamic.literal(
          Enabled = true
        )
      )
    ))

    ipfs.on("start").flatMap { _: Any =>
      Future.sequence(Seq(
        "/dns4/ams-1.bootstrap.libp2p.io/tcp/443/wss/ipfs/QmSoLer265NRgSp2LA3dPaeykiS1J6DifTC88f5uVQKNAd",
        "/dns4/sfo-1.bootstrap.libp2p.io/tcp/443/wss/ipfs/QmSoLju6m7xTh3DuokvT3886QRYqxAzb1kShaanJgW36yx",
        "/dns4/lon-1.bootstrap.libp2p.io/tcp/443/wss/ipfs/QmSoLMeWqB7YGVLJN3pNLQpmmEk35v6wYtsMGLzSr5QBU3",
        "/dns4/sfo-2.bootstrap.libp2p.io/tcp/443/wss/ipfs/QmSoLnSGccFuZQJzRadHn95W2CrSFmZuTdDWP8HXaHca9z",
        "/dns4/sfo-3.bootstrap.libp2p.io/tcp/443/wss/ipfs/QmSoLPppuBtQSGwKDZT2M73ULpjvfd3aZ6ha4oFGL1KrGM",
        "/dns4/sgp-1.bootstrap.libp2p.io/tcp/443/wss/ipfs/QmSoLSafTMBsPKadTEgaXctDQVcqN88CNLHXMkTNwMKPnu",
        "/dns4/nyc-1.bootstrap.libp2p.io/tcp/443/wss/ipfs/QmSoLueR4xBeUbY9WZ9xGUUxunbKWcrNFTDAadQJmocnWm",
        "/dns4/nyc-2.bootstrap.libp2p.io/tcp/443/wss/ipfs/QmSoLV4Bbm51jM9C4gDYZQ9Cy3U6aXMJDAbzgu2fzaDs64"
      ).map(addr => new Multiaddr(addr)).map(ipfs.swarm.connect))
    }.andThen {
      case Success(_) => println("Connected to all bootstrap nodes!")
      case Failure(e) => throw e; //TODO: Alert or sth
    }

    new AppMain(ipfs)
  }
}