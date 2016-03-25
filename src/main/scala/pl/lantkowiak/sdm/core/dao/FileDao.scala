package pl.lantkowiak.sdm.core.dao

import java.io.File

import android.util.Log
import android.webkit.MimeTypeMap
import pl.lantkowiak.sdm.utils.FileUtils
import pl.lantkowiak.simpledocumentmanager.utils.FileUtils

import scala.collection.mutable

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class FileDao(val root: File) {
  if (!root.exists()) {
    root mkdir()
  }

  def storeFiles(documentId: Int, files: mutable.LinkedHashMap[String, File]) = {
    files.foreach((e: (String, File)) => storeFile(documentId, e._1, e._2))
  }

  def storeFile(documentId: Int, fileName: String, file: File) = {
    val dir: File = createAndGetDocumentDir(documentId)
    val dst: File = new File(dir, fileName + "." + MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath))

    FileUtils.copyFile(file, dst)
  }

  private def createAndGetDocumentDir(documentId: Int): File = {
    val dir: File = new File(root, documentId + "/")
    if (!dir.exists) {
      dir.mkdir
    }
    dir
}
}
