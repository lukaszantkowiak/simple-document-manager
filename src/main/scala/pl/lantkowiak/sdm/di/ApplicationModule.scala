package pl.lantkowiak.sdm.di

import android.util.Log
import pl.lantkowiak.sdm.core.dao.{DocumentDao, DocumentDaoBean}

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
object ApplicationModule {
  private val instances = collection.mutable.Map[Class[_], Any]()

  {
    instances.put(classOf[DocumentDao], documentDAO())
  }

  def wire[T](clazz: Class[T]): T = {
    Log.e("sasasa", "ApplicationModule")

    instances.get(clazz).asInstanceOf[Some[T]].get
  }

  private def documentDAO(): DocumentDao = {
    new DocumentDaoBean(ApplicationContext.getAppContext)
  }
}
