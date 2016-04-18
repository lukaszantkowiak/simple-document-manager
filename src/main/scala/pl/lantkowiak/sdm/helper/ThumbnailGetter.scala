package pl.lantkowiak.sdm.helper

import java.io.File

import android.content.res.Resources
import android.graphics.{BitmapFactory, Bitmap}
import android.webkit.MimeTypeMap
import pl.lantkowiak.sdm.R
import pl.lantkowiak.sdm.di.ApplicationModule
import pl.lantkowiak.sdm.di.ApplicationModule.wire

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class ThumbnailGetter(val resources: Resources, val width: Int, val height: Int) {

  private lazy val bitmapHelper = wire(classOf[BitmapHelper])

  def getThumbnailForFile(file: File): Bitmap = {
    val extension: String = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath)
    val mime: String = MimeTypeMap.getSingleton.getMimeTypeFromExtension(extension)

    val bitmap: Bitmap = if (mime != null && mime.startsWith("image/"))
      bitmapHelper.retrieveBitmapFromFile(file)
    else
      BitmapFactory.decodeResource(resources, R.drawable.abc_ic_star_black_36dp)

    val isHorizontal: Boolean = bitmap.getWidth > bitmap.getHeight
    val width: Int = if (isHorizontal) this.width else this.height
    val height: Int = if (isHorizontal) this.height else this.width

    Bitmap.createScaledBitmap(bitmap, width, height, true)
  }
}
