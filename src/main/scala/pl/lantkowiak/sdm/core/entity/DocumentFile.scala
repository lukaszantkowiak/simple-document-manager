package pl.lantkowiak.sdm.core.entity

import java.util.Date

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DocumentFile(val createDate: Date, val fileName: String, val description: String) {
  var id: Int = _
}
