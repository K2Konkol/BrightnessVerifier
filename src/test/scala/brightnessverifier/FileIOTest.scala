package brightnessverifier

import java.io.File

import brightnessverifier.FileIO.{addMetadata, apply, listFiles}
import brightnessverifier.filters.{BrightnessFilter, Filter}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterEach, FlatSpec}

import scala.language.postfixOps

class FileIOTest extends FlatSpec {

  val inputPath: String = new File(ConfigFactory.load().getString("input")).getCanonicalPath
  val outputPath: String = new File(ConfigFactory.load().getString("output")).getCanonicalPath
  val brightnessFilter: Filter = new BrightnessFilter

  "listFiles" should "not be empty" in {
    assert(listFiles(inputPath) nonEmpty)
  }

  "addMetadata" should "add _<dark/bright>_<brightness int value> to file name" in {
    val file = new File("in/a.jpg")
    assert(addMetadata("in/", "out/", file,"dark",100) contains "out/a_dark_100.jpg")
  }

  "apply" should "filter a.jpg as dark" in {
    apply(inputPath, outputPath, 65, brightnessFilter)
    assert(listFiles(outputPath).toString() contains "a_dark_83.jpg")
  }
  it should "filter b.jpg as bright" in {
    assert(listFiles(outputPath).toString() contains "b_bright_51.jpg")
    listFiles(outputPath).foreach(f => f.deleteOnExit())
  }
}
