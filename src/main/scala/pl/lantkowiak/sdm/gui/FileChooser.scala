package pl.lantkowiak.sdm.gui

import java.io.File

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class FileChooser {
  private val parentDir = ".."
  private var currentPath: File = null
  var extensions: List[String] = null

}


class FileChooserBuilder {
  private val fileChooser = new FileChooser()

  def extensions(extensions: List[String]) = {
    this.fileChooser.extensions = extensions
  }
}