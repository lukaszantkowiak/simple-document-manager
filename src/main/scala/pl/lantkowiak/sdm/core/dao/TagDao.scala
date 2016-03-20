package pl.lantkowiak.sdm.core.dao

import pl.lantkowiak.sdm.core.entity.{Tag, Document}

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
trait TagDao {
  def persist(tag: Tag)

  def remainExistingNames(tags: Seq[Tag]): Seq[String]
}
