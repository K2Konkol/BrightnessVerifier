package brightnessverifier.filters

import java.awt.image.BufferedImage

abstract class Filter() {
  def filter(image: BufferedImage, cutoff: Int): (String, Int)
}
