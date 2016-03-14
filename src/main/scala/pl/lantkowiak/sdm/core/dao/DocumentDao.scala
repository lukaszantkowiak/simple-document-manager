package pl.lantkowiak.sdm.core.dao

import android.content.Context
import pl.lantkowiak.sdm.core.entities.Document

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
trait DocumentDao {
  def getRecentDocuments: List[Document]
}
