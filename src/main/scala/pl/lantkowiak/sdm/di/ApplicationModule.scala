package pl.lantkowiak.sdm.di

import android.util.Log
import pl.lantkowiak.sdm.core.dao._
import pl.lantkowiak.sdm.gui.DocumentItemCreator
import pl.lantkowiak.sdm.helper.{MessageMaker, ThumbnailGetter, BitmapHelper}

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
object ApplicationModule {
  private val instances = collection.mutable.Map[Class[_], (()=>Any, Any)]()

  {
    instances.put(classOf[DocumentDao], (documentDAO(), null))
    instances.put(classOf[DocumentItemCreator], (documentItemCreator(), null))
    instances.put(classOf[BitmapHelper], (bitmapHelper(), null))
    instances.put(classOf[SettingDao], (settingDAO(), null))
    instances.put(classOf[MessageMaker], (messageMaker(), null))
    instances.put(classOf[ThumbnailGetter], (thumbnailGetter(), null))
    instances.put(classOf[TagDao], (tagDAO(), null))
  }

  def wire[T](clazz: Class[T]): T = {
    val tuple: Option[(() => Any, Any)] = instances.get(clazz)
    if (tuple.get._2 == null) {
      instances.put(clazz, tuple.get.copy(_2 = tuple.get._1.apply()))
    }

    instances.get(clazz).get._2.asInstanceOf[T]
  }

  private def documentDAO(): () => DocumentDao = {
    () => new DocumentDaoBean(ApplicationContext.getAppContext)
  }

  private def tagDAO(): () => TagDao = {
    () => new TagDaoBean(ApplicationContext.getAppContext)
  }

  private def settingDAO(): () => SettingDao = {
    () => new SettingDaoBean()
  }

  private def documentItemCreator(): () => DocumentItemCreator = {
    () => new DocumentItemCreator(ApplicationContext.getAppContext)
  }

  private def bitmapHelper(): () => BitmapHelper = {
    () => new BitmapHelper();
  }

  private def thumbnailGetter(): () => ThumbnailGetter = {
    () =>
      val resolution: (Int, Int) = wire(classOf[SettingDao]).getThumbnailResolution
      new ThumbnailGetter(ApplicationContext.getAppContext.getResources, resolution._1, resolution._2)
  }

  private def messageMaker(): () => MessageMaker = {
    () => new MessageMaker(ApplicationContext.getAppContext);
  }
}
