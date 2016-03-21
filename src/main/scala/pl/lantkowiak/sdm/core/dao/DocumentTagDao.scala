package pl.lantkowiak.sdm.core.dao

import com.j256.ormlite.dao.{Dao, RuntimeExceptionDao}
import pl.lantkowiak.sdm.core.entity.{DocumentTag, Tag}

import scala.collection.JavaConversions._

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DocumentTagDao(val dao: Dao[DocumentTag, Integer]) extends RuntimeExceptionDao[DocumentTag, Integer](dao) {
  def persist(documentTag: DocumentTag): Unit = {
    dao.create(documentTag)
  }

  def getDocumentIdsByTagIds(tags: List[Tag]): List[DocumentTag] = {
    dao.queryBuilder.where().in("tag_id", tags).query().toList
  }
}
