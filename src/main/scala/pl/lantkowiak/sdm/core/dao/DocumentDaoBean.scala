package pl.lantkowiak.sdm.core.dao

import android.content.{ContentValues, Context}
import android.database.Cursor
import android.util.Log
import pl.lantkowiak.sdm.core.dao.db.DbHelper
import pl.lantkowiak.sdm.core.entity.{Document, Tag}
import pl.lantkowiak.sdm.di.ApplicationModule.wire

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DocumentDaoBean(val context: Context) extends DocumentDao {

  private val dbHelper = new DbHelper(context)

  private lazy val tagDao = wire(classOf[TagDao])

  private val getRecentDocumentsQuery =
    """
      select d.*, t.*
      from document d
      join document_tag dt on d.id = document_id
      join tag t on t.id = dt.tag_id
      order by create_date desc
      limit 10
    """

  override def getRecentDocuments: List[Document] = {
    val database = dbHelper.getReadableDatabase
    val query: Cursor = database.rawQuery(getRecentDocumentsQuery, null)

    Log.e("sasa", "sadsadsa " + query.getCount)

    List[Document]()
  }

  override def getDocumentsByTags(strings: Array[String]): List[Document] = {
    List[Document]()
  }

  override def getDocumentById: Document = {
    null
  }

  /**
   * Persists document and returns its id
   * @param document document to persist
   * @return id of inserted document
   */
  private def persistDocument(document: Document): Long = {
    val values = new ContentValues
    values.put("id", document.id)
    values.put("create_date", document.createDate.getTime)
    values.put("title", document.title)

    val rowId = dbHelper.getWritableDatabase.insert("document", null, values)

    if (rowId > -1) {
      val cursor: Cursor = dbHelper.getReadableDatabase.query(true, "document", Array("id"), "_id=?", Array(rowId.toString), null, null, null, null)

      cursor.moveToFirst()
      return cursor.getLong(0)
    }
    rowId
  }

  override def persist(document: Document): Unit = {
    val documentId: Long = persistDocument(document)
    persistNonExistingTags(document.tags)
  }

  private def persistNonExistingTags(tags: Seq[Tag]): Unit = {
    val existingTags = tagDao.remainExistingNames(tags)

    for (t <- tags) {
      if (!existingTags.contains(t.name)) {
        tagDao.persist(t)
      }
    }
  }
}
