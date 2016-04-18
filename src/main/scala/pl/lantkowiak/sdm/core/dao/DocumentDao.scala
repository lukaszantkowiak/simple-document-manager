package pl.lantkowiak.sdm.core.dao

import com.j256.ormlite.dao.{Dao, RuntimeExceptionDao}
import pl.lantkowiak.sdm.core.entity.Document

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DocumentDao(val dao: Dao[Document, Int]) extends RuntimeExceptionDao[Document, Int](dao) {
  def getDocumentById(documentId: Int): Document = {
    dao.queryForId(documentId)
  }

  def persist(document: Document) = {
    dao.create(document)
  }


  def getDocumentsByIds(documentIds: Iterable[Int]): List[Document] = {
    dao.queryBuilder.where().in("id", documentIds.asJava).query().toList
  }

  def getAllDocuments: List[Document] = {
    dao.queryForAll().toList
  }
}
