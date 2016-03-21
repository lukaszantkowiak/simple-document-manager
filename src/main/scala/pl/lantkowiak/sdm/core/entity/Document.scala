package pl.lantkowiak.sdm.core.entity

import java.util.Date

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
@DatabaseTable(tableName = "documents")
class Document {
  @DatabaseField(generatedId = true)
  var id: Int = _
  @DatabaseField(canBeNull = false)
  var title: String = _
  @DatabaseField(canBeNull = false)
  var createDate: Date = _

  var tags: Seq[Tag] = _
  var documentFiles: Seq[DocumentFile] = _
}
