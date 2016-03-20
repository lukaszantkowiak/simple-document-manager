package pl.lantkowiak.sdm.core.dao.db

import android.content.Context
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}

/**
 * DB helper
 *
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DbHelper(context: Context) extends SQLiteOpenHelper(context, DbConstants.DatabaseName, null, DbConstants.DatabaseVersion) {
  val createTagsTableQuery =
    """
      CREATE TABLE tag (
         id INTEGER PRIMARY KEY,
         name TEXT
      );
    """
  val createDocumentsTableQuery =
    """
      CREATE TABLE document (
         id INTEGER PRIMARY KEY,
         title TEXT,
         create_date INTEGER
      );
    """
  val createDocumentsTagsTableQuery =
    """
      CREATE TABLE document_tag (
         document_id INTEGER,
         tag_id INTEGER,
         PRIMARY KEY (document_id, tag_id),
         FOREIGN KEY(document_id) REFERENCES documents(id),
         FOREIGN KEY(tag_id) REFERENCES tags(id)
      );
    """
  val createDocumentsFileTableQuery =
    """
      CREATE TABLE document_file(
         id INTEGER PRIMARY KEY,
         document_id INTEGER,
         create_date INTEGER,
         filename TEXT,
         description TEXT,
         FOREIGN KEY(document_id) REFERENCES documents(id)
      );
    """

  override def onCreate(db: SQLiteDatabase): Unit = {
    db.execSQL(createDocumentsTableQuery)
    db.execSQL(createTagsTableQuery)
    db.execSQL(createDocumentsTagsTableQuery)
    db.execSQL(createDocumentsFileTableQuery)
  }

  override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int): Unit = {
    db.execSQL("drop table document_tag")
    db.execSQL("drop table document_file")
    db.execSQL("drop table tag")
    db.execSQL("drop table document")

    onCreate(db)
  }
}
