package brightnessverifier

import java.io.File
import java.nio.file.{Files, Paths, StandardCopyOption}

import brightnessverifier.filters.{BrightnessFilter, Filter}
import com.typesafe.config.ConfigFactory
import javax.imageio.ImageIO

object FileIO extends App {

  /*
  default cutoff, input/output values are set in resources/application.conf
  values can be passed as a command line arguments
   */
  val CUTOFF =  ConfigFactory.load().getInt("cutoff")
  val INPUTPATH = new File(ConfigFactory.load().getString("input")).getCanonicalPath
  val OUTPUTPATH = new File(ConfigFactory.load().getString("output")).getCanonicalPath

  val BRIGHTNESSFILTER: Filter = new BrightnessFilter

  // adds files to list from given input folder
  def listFiles(dir: String): List[File] = {
    val d = new File(dir).getCanonicalFile
    if (d.exists && d.isDirectory) d.listFiles().toList else List[File]()
  }

  // adds metadata to file name and changes output path
  def addMetadata(inputPath: String, outputPath: String, file: File, desc: String, intValue: Int): String = {
    val ext = file.getName.split("\\.").last
    val path = file.getCanonicalPath
    val dot = "."
    val underscore = "_"
    path.replace(inputPath, outputPath)
      .replace(dot + ext, underscore + desc + underscore + intValue + dot + ext)
  }

  /*
    copies files to destination folder
    can be changed to Files.move method to move files instead copying them
   */
  def copyImage(source: String, destination: String): Unit =
    Files.copy(
      Paths get source,
      Paths get destination,
      StandardCopyOption.REPLACE_EXISTING
    )

    // applies filter for each file in the list and copies it to output destination with metadata attached

  def apply(input: String, output: String, cutoff: Int, filter: Filter): Unit = listFiles(input)
    .foreach(file => {
      println(file)
      val filtered = filter.filter(ImageIO read file, cutoff)
      copyImage(file.getCanonicalPath, addMetadata(input, output, file, filtered._1, filtered._2))
    })

  apply(INPUTPATH, OUTPUTPATH, CUTOFF, BRIGHTNESSFILTER)
}
