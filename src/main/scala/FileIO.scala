import com.typesafe.config.ConfigFactory
import javax.imageio.ImageIO
import java.io.File
import java.nio.file.{Files, Paths, StandardCopyOption}

object FileIO extends App{

  /*
  default cutoff, input/output values set in resources/application.conf
  values can be passed as a command line arguments
   */
  val in = ConfigFactory.load().getString("input")
  val out = ConfigFactory.load().getString("output")
  val cutoff =  ConfigFactory.load().getInt("cutoff")
  val inputPath = new File(in).getCanonicalPath
  val outputPath = new File(out).getCanonicalPath

  // adds input files to list
  def listFiles(dir: String):List[File] = {
    val d = new File(dir).getCanonicalFile
    if (d.exists && d.isDirectory) d.listFiles().toList else List[File]()
  }

  // changes output path and adds metadata to file name
  def addMetadata(file: File, desc: String, intValue: Int): String = {
    val ext = file.getName.split("\\.").last
    val path = file.getCanonicalPath
    path.replace(inputPath, outputPath)
        .replace("." + ext,"_" + desc + "_" + intValue + "." + ext)
  }

  /* copies files to destination folder
     can be changed to Files.move method to move files instead copying them
   */
  def copyImage(source: String, destination: String): Unit =
    Files.copy(
      Paths get source,
      Paths get destination,
      StandardCopyOption.REPLACE_EXISTING
    )

    listFiles(inputPath)
    .foreach(file => {
      println(file)
      val filtered = ImageFilter.filter(ImageIO read file, cutoff)
      copyImage(file.getCanonicalPath, addMetadata(file, filtered._1, filtered._2))
    })
}
