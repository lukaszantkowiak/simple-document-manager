package pl.lantkowiak.sdm.core.dao

import java.io.File

import pl.lantkowiak.sdm.core.dao.FileDao

import scala.collection.mutable

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class FileDao(val root: File) {
  if (!root.exists()) {
    root mkdir()
  }

  def storeFiles(id: Int, files: mutable.LinkedHashMap[String, File]) = {}
}
