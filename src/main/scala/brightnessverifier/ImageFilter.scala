package brightnessverifier

import java.awt.image.BufferedImage

abstract class ImageFilter() {
  def filter(image: BufferedImage, cutoff: Int): (String, Int)
}
