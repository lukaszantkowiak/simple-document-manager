package pl.lantkowiak.sdm.core.entities

import java.util.Date

/**
 * Document file entity
 *
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DocumentFile(val id: Int, val documentId: Int, val createDate: Date, val fileName: String, val description: String) {
}
