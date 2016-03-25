package pl.lantkowiak.sdm.core.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
@DatabaseTable(tableName = "documents_tags")
class DocumentTag {
  @DatabaseField(generatedId = true)
  var id: Int = _
  @DatabaseField(foreign = true, canBeNull = false)
  var document: Document = _
  @DatabaseField(foreign = true, canBeNull = false)
  var tag: Tag = _
}
