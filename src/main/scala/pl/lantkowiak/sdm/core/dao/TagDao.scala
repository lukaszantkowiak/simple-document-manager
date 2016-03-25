package pl.lantkowiak.sdm.core.dao

import com.j256.ormlite.dao.{Dao, RuntimeExceptionDao}
import pl.lantkowiak.sdm.core.entity.Tag

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class TagDao(val dao: Dao[Tag, Integer]) extends RuntimeExceptionDao[Tag, Integer](dao) {
  def getTagsById(ids: List[Int]): List[Tag] = {
    dao.queryBuilder.where().in("id", ids.asJava).query().toList
  }

  def createIfNotExistsAndGetTagByName(name: String): Tag = {
    var tag: Tag = getTagByName(name)
    if (tag == null) {
      tag = new Tag
      tag.name = name
      persist(tag)
    }

    tag
  }

  def persist(tag: Tag) {
    dao.create(tag)
  }

  def getTagByName(name: String): Tag = {
    dao.queryBuilder.where.eq("name", name).queryForFirst
  }

  def getTagsByNames(tagNames: List[String]): List[Tag] = {
    dao.queryBuilder().where().in("name", tagNames).query().toList
  }
}
