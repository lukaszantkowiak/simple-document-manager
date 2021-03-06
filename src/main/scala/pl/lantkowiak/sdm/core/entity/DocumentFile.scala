package pl.lantkowiak.sdm.core.entity

import java.util.Date

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
@DatabaseTable(tableName = "documentFiles")
class DocumentFile {
  @DatabaseField(generatedId = true)
  var id: Int = 0
  @DatabaseField(canBeNull = false)
  var createDate: Date = _
  @DatabaseField(canBeNull = false)
  var fileId: Int = 0
  @DatabaseField(canBeNull = false)
  var filename: String = _
  @DatabaseField(canBeNull = false)
  var extension: String = _
  @DatabaseField(canBeNull = true)
  var mime: String = _
  @DatabaseField(canBeNull = false)
  var description: String = _
  @DatabaseField(foreign = true, canBeNull = false)
  var document: Document = _

  def fullFilename : String = {
    filename + "." + extension
  }

  def storeFilename : String = {
    fileId + "." + extension
  }
}
