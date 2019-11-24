package brightnessverifier

import java.awt.image.BufferedImage

import org.scalatest.{FlatSpec, FunSuite}

class ImageFilterTest extends FlatSpec {

  val black: BufferedImage = new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB)
  black.setRGB(0,0,0)
  val white: BufferedImage = new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB)
  white.setRGB(0,0,255)
  val color: BufferedImage = new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB)
  color.setRGB(0,0,125)


  "A filter with cutoff 50" should "mark black image as dark and give it value 100" in {
    assert(ImageFilter.filter(black, 50) == ("dark", 100))
  }
  it should "mark white image as bright and give it value 0" in {
    assert(ImageFilter.filter(white, 50) == ("bright", 0))
  }
  it should "mark color image as dark and give it value 50" in {
    assert(ImageFilter.filter(color, 50) == ("dark", 50))
  }
  it should "mark the same color image as bright with cutoff changed to 51" in {
    assert(ImageFilter.filter(color, 51) == ("bright", 50))
  }

}
