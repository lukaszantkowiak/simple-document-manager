package pl.lantkowiak.sdm.gui

import java.io.{File, FileFilter}

import android.app.{Activity, Dialog}
import android.os.Environment
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.{View, ViewGroup}
import android.webkit.MimeTypeMap
import android.widget.{AdapterView, ArrayAdapter, ListView, TextView}
import pl.lantkowiak.sdm.di.ApplicationModule.wire
import pl.lantkowiak.sdm.helper.MessageMaker

import scala.collection.mutable.ArrayBuffer


/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class FileChooser(val activity: Activity, val fileListener: FileSelectedListener, val maxFileSize: Double, val extensions: List[String]) {
  private val parentDir = ".."
  private val dialog = new Dialog(activity)
  private val list = new ListView(activity)
  private lazy val messageMaker = wire(classOf[MessageMaker])
  private var currentPath: File = _

  list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    override def onItemClick(parent: AdapterView[_], view: View, position: Int, id: Long): Unit = {
      val fileChosen: String = list.getItemAtPosition(position).asInstanceOf[String]
      val chosenFile: File = getChosenFile(fileChosen)
      if (chosenFile.isDirectory) {
        refresh(chosenFile)
      }
      else {
        val sizeInMB: Double = chosenFile.length / (1024.0 * 1024.0)
        if (sizeInMB > maxFileSize) {
          messageMaker.info("Selected file is too large")
        }
        else if (extensions != null && !extensions.contains(MimeTypeMap.getFileExtensionFromUrl(chosenFile.getAbsolutePath))) {
          messageMaker.info("Selected file has not allowed extension")
        }
        else {
          if (fileListener != null) {
            fileListener.fileSelected(chosenFile)
          }
          dialog.dismiss()
        }
      }
    }
  })
  dialog.setContentView(list)
  dialog.getWindow.setLayout(MATCH_PARENT, MATCH_PARENT)
  refresh(Environment.getExternalStorageDirectory)

  def showDialog() {
    dialog.show()
  }

  /**
   * Sort, filter and display the files for the given path.
   */
  private def refresh(path: File) {
    this.currentPath = path
    if (path.exists) {
      val dirs: Array[File] = path.listFiles(new FileFilter() {
        def accept(file: File): Boolean = {
          file.isDirectory && file.canRead
        }
      })
      val files: Array[File] = path.listFiles(new FileFilter() {
        def accept(file: File): Boolean = {
          !file.isDirectory && file.canRead
        }
      })
      dirs.sortWith(_.getName > _.getName)
      files.sortWith(_.getName > _.getName)

      // convert to an array
      val i: Int = 0
      var fileList = ArrayBuffer[String]()

      if (path.getParentFile != null) {
        fileList += parentDir
      }
      dirs.foreach(d => fileList += d.getName)
      files.foreach(f => fileList += f.getName)

      // refresh the user interface
      dialog.setTitle(currentPath.getPath)
      list.setAdapter(new ArrayAdapter(activity, android.R.layout.simple_list_item_1, fileList.toArray) {
        override def getView(pos: Int, view: View, parent: ViewGroup): View = {
          val superView = super.getView(pos, view, parent)
          superView.asInstanceOf[TextView].setSingleLine(true)
          superView
        }
      })
    }
  }

  /**
   * Convert a relative filename into an actual File object.
   */
  private def getChosenFile(fileChosen: String): File = {
    if (fileChosen == parentDir) {
      currentPath.getParentFile
    }
    else {
      new File(currentPath, fileChosen)
    }
  }
}

trait FileSelectedListener {
  def fileSelected(file: File)
}
