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
      val inStream = new FileInputStream(src)
      val outStream = new FileOutputStream(dst)
      val inChannel = inStream.getChannel
      val outChannel = outStream.getChannel
      inChannel.transferTo(0, inChannel.size, outChannel)
      inStream.close()
      outStream.close()
    } catch {
      case e: Exception => Log.e(FileUtils.getClass.getSimpleName, e.getMessage, e)
    }
  }

}
