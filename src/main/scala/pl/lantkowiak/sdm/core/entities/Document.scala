package pl.lantkowiak.sdm.core.entities

import java.util.Date

/**
 * Document entity
 *
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class Document(val id: Int, val title: String, val createDate: Date, val tags: List[String], val documentFiles: List[DocumentFile]) {
}
