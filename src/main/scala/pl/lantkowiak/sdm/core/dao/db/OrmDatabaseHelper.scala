package pl.lantkowiak.sdm.core.dao.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import pl.lantkowiak.sdm.core.entity.{Document, DocumentFile, DocumentTag, Tag}

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class OrmDatabaseHelper(val context: Context) extends OrmLiteSqliteOpenHelper(context, DbConstants.DatabaseName, null, DbConstants.DatabaseVersion) {
  override def onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource): Unit = {
    TableUtils.createTable(connectionSource, classOf[Tag])
    TableUtils.createTable(connectionSource, classOf[Document])
    TableUtils.createTable(connectionSource, classOf[DocumentFile])
    TableUtils.createTable(connectionSource, classOf[DocumentTag])
  }

  override def onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int): Unit = {
    try {
      TableUtils.dropTable(connectionSource, classOf[DocumentFile], true)
      TableUtils.dropTable(connectionSource, classOf[DocumentTag], true)
      TableUtils.dropTable(connectionSource, classOf[Tag], true)
      TableUtils.dropTable(connectionSource, classOf[Document], true)
    } catch {
      case e: Exception => Log.e("Tables cannot be dropped!", e.getMessage, e)
    }

    onCreate(database, connectionSource)
  }
}
