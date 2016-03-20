package pl.lantkowiak.sdm.core.dao

import android.content.{ContentValues, Context}
import android.database.Cursor
import android.util.Log
import pl.lantkowiak.sdm.core.dao.db.DbHelper
import pl.lantkowiak.sdm.core.entity.{Document, Tag}

import scala.collection.mutable.ArrayBuffer

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class TagDaoBean(val context: Context) extends TagDao {

  private val dbHelper = new DbHelper(context)

  private val getTagsByNameQuery =
    """
      select name
      from tag t
      where t.name in (%s)
    """

  override def persist(t: Tag) = {
    val values = new ContentValues
    values.put("id", t.id.asInstanceOf[Integer])
    values.put("name", t.name)

    dbHelper.getWritableDatabase.insert("tag", null, values)
  }

  override def remainExistingNames(tags: Seq[Tag]): Seq[String] = {
    val tagsToQuery = new StringBuilder
    tags.foreach(t => tagsToQuery append '\'' append t.name append '\'' append ',')
    tagsToQuery deleteCharAt (tagsToQuery.length - 1)

    val tagsSql: String = String.format(getTagsByNameQuery, tagsToQuery)
    val query: Cursor = dbHelper.getReadableDatabase.rawQuery(tagsSql, null)
    val existingTags = new ArrayBuffer[String]()
    while (query.moveToNext()) {
      existingTags.append(query.getString(0))
    }

    existingTags
  }
}
