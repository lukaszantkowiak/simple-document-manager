package pl.lantkowiak.sdm.core.dao

import java.util

import com.j256.ormlite.dao.{Dao, RuntimeExceptionDao}
import pl.lantkowiak.sdm.core.entity.Document
import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DocumentDao(val dao: Dao[Document, Integer]) extends RuntimeExceptionDao[Document, Integer](dao) {
  def persist(document: Document) = {
    dao.create(document)
  }


  def getDocumentsByIds(documentIds: ListBuffer[Integer]): List[Document] = {
    dao.queryBuilder.where().in("id", documentIds).query().toList
  }

  def getAllDocuments: List[Document] = {
    dao.queryForAll().toList
  }
}
