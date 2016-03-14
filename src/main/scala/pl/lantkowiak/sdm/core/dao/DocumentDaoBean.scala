package pl.lantkowiak.sdm.core.dao

import android.content.Context
import android.database.Cursor
import android.util.Log
import pl.lantkowiak.sdm.core.db.DbHelper
import pl.lantkowiak.sdm.core.entities.Document

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DocumentDaoBean(val context: Context) extends DocumentDao {

  val dbHelper = new DbHelper(context)

  val getRecentDocumentsQuery =
    """
      select *
      from document d
      order by create_date desc
      limit 10
    """

  override def getRecentDocuments: List[Document] = {
    val database = dbHelper.getReadableDatabase
    val query: Cursor = database.rawQuery(getRecentDocumentsQuery, null)

    Log.e("sasa","sadsadsa "+query.getCount)

    null
  }
}
