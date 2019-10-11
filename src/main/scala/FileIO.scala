
import java.awt.Color

import javax.imageio.ImageIO

object FileIO extends App {

  def parse(array: Float): Int = ((1-array)*100).intValue
  def avg(value: IndexedSeq[Int]) = (value.sum / value.length).intValue

  val path = getClass getResourceAsStream "/in/l.jpg"
  val img = ImageIO read path

  val colorArray = avg(for {
    x <- 0 until img.getWidth-1
    y <- 0 until img.getHeight-1
  } yield parse(Color.RGBtoHSB(
    new Color(img.getRGB(x,y)).getRed,
    new Color(img.getRGB(x,y)).getGreen,
    new Color(img.getRGB(x,y)).getBlue,
    null)(2)))

  println(colorArray)

}

