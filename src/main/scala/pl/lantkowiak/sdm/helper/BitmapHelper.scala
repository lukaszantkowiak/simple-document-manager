package pl.lantkowiak.sdm.helper

import java.io.File

import android.graphics.{BitmapFactory, Bitmap}

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class BitmapHelper {
  def retrieveBitmapFromFile(file: File): Bitmap = {
    val bmOptions: BitmapFactory.Options = new BitmapFactory.Options
    BitmapFactory.decodeFile(file.getAbsolutePath, bmOptions)
  }
}
