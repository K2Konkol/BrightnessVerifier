
import java.awt.Color
import java.awt.image.BufferedImage

object ImageFilter {

  private def parse(array: Float): Int = ((1-array)*100).intValue
  private def avg(value: IndexedSeq[Int]) = (value.sum / value.length).intValue

  def filter(image: BufferedImage, cutoff: Int): (String, Int) = {
    val avgBrightness = avg(for {
      x <- 0 until image.getWidth
      y <- 0 until image.getHeight
    } yield parse(Color.RGBtoHSB(
      new Color(image.getRGB(x,y)).getRed,
      new Color(image.getRGB(x,y)).getGreen,
      new Color(image.getRGB(x,y)).getBlue,
      null)(2)))
    (if (avgBrightness<cutoff) "bright" else "dark",
      avgBrightness)
  }




}

