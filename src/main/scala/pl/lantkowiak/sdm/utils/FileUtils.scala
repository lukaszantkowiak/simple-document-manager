package pl.lantkowiak.sdm.utils

import java.io.{FileOutputStream, FileInputStream, File}
import java.nio.channels.FileChannel

import android.util.Log

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
object FileUtils {
  def copyFile(src: File, dst: File) = {
    try {
      val inStream: FileInputStream = new FileInputStream(src)
      val outStream: FileOutputStream = new FileOutputStream(dst)
      val inChannel: FileChannel = inStream.getChannel
      val outChannel: FileChannel = outStream.getChannel
      inChannel.transferTo(0, inChannel.size, outChannel)
      inStream.close()
      outStream.close()
    } catch {
      case e: Exception => Log.e(FileUtils.getClass.getSimpleName, e.getMessage)
    }
  }

}
