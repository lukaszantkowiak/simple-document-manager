package pl.lantkowiak.sdm.core.entity

import java.util.Date

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
@DatabaseTable(tableName = "documentFiles")
class DocumentFile() {
  @DatabaseField(generatedId = true)
  var id: Int = _
  @DatabaseField(canBeNull = false)
  var createDate: Date = _
  @DatabaseField(canBeNull = false)
  var fileName: String = _
  @DatabaseField(canBeNull = false)
  var extension: String = _
  @DatabaseField(canBeNull = false)
  var mime: String = _
  @DatabaseField(canBeNull = false)
  var description: String = _
}
