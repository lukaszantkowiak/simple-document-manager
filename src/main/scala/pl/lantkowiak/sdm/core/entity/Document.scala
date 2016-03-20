package pl.lantkowiak.sdm.core.entity

import java.util.Date

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class Document(val title: String, val createDate: Date, val tags: Seq[Tag]) {
  var id: Int = _
  var documentFiles : Seq[DocumentFile] = _
}
