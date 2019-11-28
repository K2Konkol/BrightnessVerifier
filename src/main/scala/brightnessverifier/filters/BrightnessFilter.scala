package brightnessverifier.filters

import java.awt.Color
import java.awt.image.BufferedImage

class BrightnessFilter extends Filter {

  // converts brightness value from 0 - 1 (dark to bright) to 0 - 100 scale (bright to dark)
  private def parse(value: Float): Int = ((1-value)*100).intValue

  // takes average value from all the pixels
  private def avg(value: IndexedSeq[Int]) = (value.sum / value.length).intValue

  /*
  method iterates through all the pixels in the image, converts them from RGB to HSB
  and returns metadata, bright/dark and brightness int value as a tuple
   */
  def filter(image: BufferedImage, cutoff: Int): (String, Int) = {
    val avgBrightness = avg(for {
      x <- 0 until image.getWidth
      y <- 0 until image.getHeight
    } yield {
      val pixel = new Color(image.getRGB(x,y))
      parse(value = Color.RGBtoHSB(pixel.getRed, pixel.getGreen, pixel.getBlue, null)(2))
    })
    (if (avgBrightness<cutoff) "bright" else "dark",
      avgBrightness)
  }




}
