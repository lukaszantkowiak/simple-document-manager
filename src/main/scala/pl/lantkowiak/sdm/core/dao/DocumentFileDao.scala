package pl.lantkowiak.sdm.core.dao

import java.util

import com.j256.ormlite.dao.{Dao, RuntimeExceptionDao}
import pl.lantkowiak.sdm.core.entity.{DocumentFile, Tag}

import scala.collection.JavaConversions._

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DocumentFileDao(val dao: Dao[DocumentFile, Integer]) extends RuntimeExceptionDao[DocumentFile, Integer](dao) {
  def getFilesByDocumentId(documentId: Int): List[DocumentFile] = {
    val documentFiles = dao.queryForEq("document_id", documentId).toList
    documentFiles.sortWith((a, b) => a.createDate.compareTo(b.createDate) > 0)

    documentFiles
  }

  def persist(documentFile: DocumentFile): Unit = {
    dao.create(documentFile)
  }
}
