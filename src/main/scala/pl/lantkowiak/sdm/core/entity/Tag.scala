package pl.lantkowiak.sdm.core.entity

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
@DatabaseTable(tableName = "tags")
class Tag {
  @DatabaseField(generatedId = true)
  var id: Int = _
  @DatabaseField(canBeNull = false)
  var name: String = _
}
