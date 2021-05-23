package brightnessverifier

import java.io.File
import java.nio.file.{Files, Paths, StandardCopyOption}

import brightnessverifier.filters.{BrightnessFilter, Filter}
import com.typesafe.config.ConfigFactory
import javax.imageio.ImageIO

import com.rabbitmq.client.{CancelCallback, ConnectionFactory, DeliverCallback}

object FileIO extends App {

  /*
   default cutoff, input/output values are set in resources/application.conf
   values can be passed as a command line arguments
  */
  val CUTOFF = ConfigFactory.load().getInt("cutoff")
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

  // apply(INPUTPATH, OUTPUTPATH, CUTOFF, BRIGHTNESSFILTER)

  def filter(filename: String, input: String, output: String, cutoff: Int, filter: Filter): Unit = {
    val file = new File(INPUTPATH + '/' + filename).getCanonicalFile
    val filtered = filter.filter(ImageIO read file, cutoff)
    copyImage(file.getCanonicalPath, addMetadata(input, output, file, filtered._1, filtered._2))
  }


  val QUEUE_NAME = "messages"

  val factory = new ConnectionFactory()
  factory.setHost("mybroker")

  val connection = factory.newConnection()
  val channel = connection.createChannel()

  try {
    channel.queueDeclare(QUEUE_NAME, true, false, false, null)
  } catch {
    case e: Exception => {
      Thread.sleep(1000)
      System.exit(1)
    }
  }

  println(s"waiting for messages on $QUEUE_NAME")

  /*
  * This tells RabbitMQ not to give more than one message to a worker at a time.
  * Or, in other words, don't dispatch a new message to a worker
  * until it has processed and acknowledged the previous one.
  * Instead, it will dispatch it to the next worker that is not still busy.
  * */
  val prefetchCount = 1
  channel.basicQos(prefetchCount)

  val callback: DeliverCallback = (consumerTag, delivery) => {
    val fileName = new String(delivery.getBody, "UTF-8")
    try {
      println(s"Received $fileName with tag $consumerTag")
      filter(fileName, INPUTPATH, OUTPUTPATH, CUTOFF, BRIGHTNESSFILTER)
    } finally {
      println(s"Filtered $fileName")
      channel.basicAck(delivery.getEnvelope.getDeliveryTag, false)
    }
  }


  val cancel: CancelCallback = consumerTag => {}

  val autoAck = false

  channel.basicConsume(QUEUE_NAME, autoAck, callback, cancel)

  while (true) {
    // we don't want to kill the receiver,
    // so we keep him alive waiting for more messages
    Thread.sleep(1000)
  }

  channel.close()
  connection.close()
}



