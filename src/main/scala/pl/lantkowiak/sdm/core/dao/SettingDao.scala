package pl.lantkowiak.sdm.core.dao

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
trait SettingDao {
  def getAppPath: String

  def getMaxFileSize: Double

  def getAllowedExtensions: List[String]

  def getThumbnailResolution: (Int, Int)
}
