package pl.lantkowiak.sdm.core.dao

import java.util

import android.util.Log
import com.j256.ormlite.dao.{Dao, RuntimeExceptionDao}
import com.j256.ormlite.stmt.DeleteBuilder
import pl.lantkowiak.sdm.core.entity.{Document, DocumentTag, Tag}

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DocumentTagDao(val dao: Dao[DocumentTag, Int]) extends RuntimeExceptionDao[DocumentTag, Int](dao) {
  def removeByDocumentIdAndTagNames(document: Document, tags: List[Tag]): Unit = {
    val deleteBuilder: DeleteBuilder[DocumentTag, Int] = dao.deleteBuilder()
    deleteBuilder.where.eq("document_id", document).and.in("tag_id", tags.asJava)
    deleteBuilder.delete
  }

  def persist(documentTag: DocumentTag): Unit = {
    dao.create(documentTag)
  }

  def getDocumentIdsByTagIds(tags: List[Tag]): List[DocumentTag] = {
    dao.queryBuilder.where().in("tag_id", tags.asJava).query().toList
  }
}
