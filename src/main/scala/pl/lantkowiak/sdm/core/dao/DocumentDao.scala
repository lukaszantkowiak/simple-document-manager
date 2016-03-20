package pl.lantkowiak.sdm.core.dao

import pl.lantkowiak.sdm.core.entity.Document

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
trait DocumentDao {
  def getDocumentsByTags(strings: Array[String]): List[Document]

  def getRecentDocuments: List[Document]

  def getDocumentById: Document

  def persist(document: Document)
}
