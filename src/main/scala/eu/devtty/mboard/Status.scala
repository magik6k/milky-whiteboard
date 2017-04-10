package eu.devtty.mboard

import org.scalajs.dom.document

object Status {
  def status(msg: String): Unit = {
    document.getElementById("status").innerHTML = msg
  }

  def done(): Unit = {
    document.getElementById("boot-cover").parentNode.removeChild(document.getElementById("boot-cover"))
  }
}
