package eu.devtty.mboard.canvas

import eu.devtty.ipfs.IpfsNode
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw._

class Canvas(name: String, ipfs: IpfsNode, room: String) {
  private val INITIAL_WIDTH = 1600.0
  private val INITIAL_HEIGHT = 1200.0

  private val chain = new Chain(ipfs)
  private val broadcaster = new CanvasBroadcaster(ipfs, chain, room)
  private val subscriber = new CanvasSubscriber(ipfs, chain, room, this)

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
      val (posX, posY) = getPos(e)
      startX = posX
      startY = posY
      currentCtx.fillRect(startX - 1, startY - 1, 3, 3)
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
      val (posX, posY) = getPos(e)
      startX = posX
      startY = posY
    }
  }

  input.onmouseup = { e: MouseEvent =>
    commit()
  }

  private def getPos(e: MouseEvent): (Double, Double) = {
    val raw = (e.clientX - input.offsetLeft, e.clientY - input.offsetTop)
    (raw._1 * (INITIAL_WIDTH / mainCanvas.clientWidth), raw._2 * (INITIAL_HEIGHT / mainCanvas.clientHeight))
  }

  private def draw(e: MouseEvent): Unit = {
    if((e.buttons & 1) == 1 && startX > 0) {
      val (curX, curY) = getPos(e)

      currentCtx.beginPath()
      currentCtx.moveTo(startX, startY)
      currentCtx.lineTo(curX, curY)
      currentCtx.stroke()
      currentCtx.closePath()

      broadcaster.live((startX, startY), (curX, curY))

      startX = curX
      startY = curY
      clean = false
    }
  }

  private def commit(): Unit = {
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

  def drawImage(data: String, peer: String): Unit = {
    val img = document.createElement("img").asInstanceOf[HTMLImageElement]
    img.onload = { e: Event =>
      mainCtx.drawImage(img, 0, 0)
      if(peer != null)
        freeTempCanvas(peer)
    }

    img.src = data


  }

  def drawPart(startX: Double, startY: Double, endX: Double, endY: Double, peer: String): Unit = {
    val ctx = getTempCanvas(peer)

    ctx.beginPath()
    ctx.moveTo(startX, startY)
    ctx.lineTo(endX, endY)
    ctx.stroke()
    ctx.closePath()
  }

  private def freeTempCanvas(peer: String): Unit = {
    val canvas = document.getElementById(s"$name-$peer")
    if(canvas != null)
      canvas.parentNode.removeChild(canvas)
  }

  private def getTempCanvas(peer: String): CanvasRenderingContext2D = {
    var canvasElement = document.getElementById(s"$name-$peer").asInstanceOf[HTMLCanvasElement]
    if(canvasElement == null) {
      canvasElement = document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
      canvasElement.id = s"$name-$peer"
      canvasElement.classList.add("canvas-layer")
      canvasElement.width = INITIAL_WIDTH.toInt
      canvasElement.height = INITIAL_HEIGHT.toInt

      input.appendChild(canvasElement)
    }

    canvasElement.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
  }
}
