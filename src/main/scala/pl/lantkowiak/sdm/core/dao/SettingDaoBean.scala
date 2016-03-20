package pl.lantkowiak.sdm.core.dao

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class SettingDaoBean extends SettingDao {
  override def getAppPath: String = {
    "/files/"
  }

  override def getThumbnailResolution: (Int, Int) = {
    (200, 150)
  }

  override def getAllowedExtensions: List[String] = {
    List("jpg", "jpeg", "png", "gif", "pdf", "doc", "docs", "xls", "xlsx", "ppt", "pptx", "txt")
  }

  override def getMaxFileSize: Double = {
    5.0
  }
}
