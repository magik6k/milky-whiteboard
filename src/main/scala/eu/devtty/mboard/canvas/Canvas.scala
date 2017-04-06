package eu.devtty.mboard.canvas

import eu.devtty.ipfs.IpfsNode
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw._

class Canvas(name: String, ipfs: IpfsNode, room: String) {
  private val broadcaster = new CanvasBroadcaster(ipfs, room)
  private val subscriber = new CanvasSubscriber(ipfs, room)

  private val mainCanvas = document.getElementById(s"$name-canvas").asInstanceOf[HTMLCanvasElement]
  private val currentCanvas = document.getElementById(s"$name-current").asInstanceOf[HTMLCanvasElement]
  private val input = document.getElementById(s"$name-input").asInstanceOf[HTMLElement]

  private val mainCtx = mainCanvas.getContext("2d")
  private val currentCtx = currentCanvas.getContext("2d")

  currentCtx.clearRect(0, 0, mainCanvas.width, mainCanvas.height)
  currentCtx.fillStyle = "rgba(255,0,0,1)"
  currentCtx.lineWidth = 3
  currentCtx.strokeStyle = "rgba(255,0,0,1)"

  var startX, startY = -1.0
  var clean = true

  input.onmousedown = { e: dom.MouseEvent =>
    if(e.button == 0) {
      currentCtx.fillRect(e.clientX - input.offsetLeft - 1, e.clientY - input.offsetTop - 1, 3, 3)
      startX = e.clientX - input.offsetLeft
      startY = e.clientY - input.offsetTop
      clean = false
    }
  }

  input.onmousemove = { e: dom.MouseEvent =>
    draw(e)
  }

  input.onmouseleave = { e: dom.MouseEvent =>
    draw(e)
    commit()
  }

  input.onmouseenter = { e: dom.MouseEvent =>
    if((e.buttons & 1) == 1) {
      startX = e.clientX - input.offsetLeft
      startY = e.clientY - input.offsetTop
    }
  }

  input.onmouseup = { e: MouseEvent =>
    commit()
  }

  def draw(e: MouseEvent): Unit = {
    if((e.buttons & 1) == 1 && startX > 0) {
      val curX = e.clientX - input.offsetLeft
      val curY = e.clientY - input.offsetTop

      currentCtx.beginPath()
      currentCtx.moveTo(startX, startY)
      currentCtx.lineTo(curX, curY)
      currentCtx.stroke()
      currentCtx.closePath()

      startX = curX
      startY = curY
      clean = false
    }
  }

  def commit(): Unit = {
    if(clean)
      return

    startX = -1
    startY = -1
    clean = true

    val data = currentCanvas.toDataURL("image/png")
    val img = document.createElement("img").asInstanceOf[HTMLImageElement]
    img.onload = { e: Event =>
      mainCtx.drawImage(img, 0, 0)
      currentCtx.clearRect(0, 0, mainCanvas.width, mainCanvas.height)
    }

    img.src = data

    broadcaster.commit(data)
  }
}
