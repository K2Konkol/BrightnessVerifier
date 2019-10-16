import com.typesafe.config.ConfigFactory
import javax.imageio.ImageIO
import java.io.File
import java.net.URL
import java.nio.file.{Files, Paths, StandardCopyOption}

object FileIO extends App{

  val in = ConfigFactory.load().getString("path.input")
  val out = ConfigFactory.load().getString("path.output")
  val url = getClass getResource in


  def applyFilter(dir: URL):List[File] = {
    val d = new File(dir.getPath)
    if (d.exists && d.isDirectory) d.listFiles().toList else List[File]()
  }

  def addMetadata(file: File, desc: String, intValue: Int): String = {
    val ext = file.getName.split("\\.").last
    val path = file.getAbsolutePath
    path.replace(in,out)
        .replace("." + ext,"_" + desc + "_" + intValue + "." + ext)
  }

  def copyImage(source: String, destination: String): Unit =
    Files.copy(
      Paths get source,
      Paths get destination,
      StandardCopyOption.REPLACE_EXISTING
    )

    applyFilter(url)
    .foreach(f => {
      println(f)
      val filtered = ImageFilter.filter(ImageIO read f, ConfigFactory.load().getInt("cutoff"))
      copyImage(f.getPath, addMetadata(f, filtered._1, filtered._2))
    })
}
