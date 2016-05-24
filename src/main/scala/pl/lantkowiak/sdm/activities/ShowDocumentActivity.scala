package pl.lantkowiak.sdm.activities

import android.app.Activity
import android.content.{ActivityNotFoundException, DialogInterface, Intent}
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.{AlertDialog, AppCompatActivity}
import android.util.Log
import android.view.ViewGroup.LayoutParams.{MATCH_PARENT, WRAP_CONTENT}
import android.view.{Gravity, Menu, MenuItem, View}
import android.widget.TableRow.LayoutParams
import android.widget._
import pl.lantkowiak.sdm.R
import pl.lantkowiak.sdm.core.dao.{DocumentDao, DocumentFileDao, FileDao, TagDao}
import pl.lantkowiak.sdm.core.entity.{Document, DocumentFile, Tag}
import pl.lantkowiak.sdm.di.ApplicationModule.wire
import pl.lantkowiak.sdm.helper.{MessageMaker, ThumbnailGetter}

import scala.collection.JavaConversions._

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class ShowDocumentActivity extends AppCompatActivity {
  private lazy val documentDao = wire(classOf[DocumentDao])
  private lazy val tagDao = wire(classOf[TagDao])
  private lazy val documentFileDao = wire(classOf[DocumentFileDao])
  private lazy val fileDao = wire(classOf[FileDao])
  private lazy val thumbnailGetter = wire(classOf[ThumbnailGetter])
  private lazy val messageMaker = wire(classOf[MessageMaker])

  var document: Document = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_show_document)
    document = getDocument
    loadDocument()
  }

  protected override def onResume() {
    document = documentDao.getDocumentById(document.id) // TODO: check this
    loadDocument()
    super.onResume()
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu_show_document, menu)
    true
  }

  private def loadDocument() = {
    loadTitle()
    loadTags()
    loadFiles()
  }

  private def loadTitle() {
    setTitle(document.title)
  }

  private def loadTags() {
    val sb = new StringBuilder

    val tags: List[Tag] = tagDao.getTagsByIds(document.documentTags.map(t => t.tag.id).toList)
    tags.foreach(t => sb.append(t.name).append(" "))

    findViewById(R.id.show_document_tags).asInstanceOf[TextView].setText(sb.toString())
  }

  private def loadFiles() {
    val filesLayout = findViewById(R.id.show_document_files_table).asInstanceOf[TableLayout]
    filesLayout.removeAllViews()
    val documentFiles: List[DocumentFile] = documentFileDao.getFilesByDocumentId(document.id)
    Log.e("filesLayout.size1", filesLayout.getChildCount.toString)
    for (documentFile <- documentFiles) {
      val scale = this.getResources.getDisplayMetrics.density
      val dim110 = (100 * scale + 0.5f).toInt

      val tableRow = new TableRow(this)
      tableRow.setLayoutParams(new TableLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

      val imageFrameLayout = new FrameLayout(this)
      imageFrameLayout.setLayoutParams(new TableRow.LayoutParams(dim110, dim110))

      val imageView: ImageView = new ImageView(this)
      val file = fileDao.getFile(document.id, documentFile.storeFilename)
      val thumbnail = thumbnailGetter.getThumbnailForFile(file)
      imageView.setImageBitmap(thumbnail)
      val downloadFileDialog = new DownloadFileDialog(document.id, documentFile.storeFilename, documentFile.filename)
      val fileActionClickListener = new FileActionClickListener(this, file.getAbsolutePath, documentFile.mime, downloadFileDialog)
      imageView.setOnClickListener(fileActionClickListener)
      val layoutParams = new FrameLayout.LayoutParams(thumbnail.getWidth, thumbnail.getHeight)
      layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL
      imageView.setLayoutParams(layoutParams)

      imageFrameLayout.addView(imageView)

      tableRow.addView(imageFrameLayout)


      val fileDescription = new TextView(this)
      val fileDescriptionParams: LayoutParams = new TableRow.LayoutParams(WRAP_CONTENT, dim110, 0.1f)
      fileDescriptionParams.gravity = Gravity.CENTER_VERTICAL
      fileDescription.setLayoutParams(fileDescriptionParams)
      fileDescription.setText(documentFile.description)

      tableRow.addView(fileDescription)

      filesLayout.addView(tableRow)
    }

    Log.e("filesLayout.size2", filesLayout.getChildCount.toString)
  }

  private def getDocument: Document = {
    val documentId: Int = getIntent.getExtras.getInt("documentId")
    documentDao.getDocumentById(documentId)
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    val id: Int = item.getItemId
    if (id == R.id.show_document_edit_document) {
      return editDocument
    }
    super.onOptionsItemSelected(item)
  }

  private def editDocument: Boolean = {
    val intent: Intent = new Intent(this, classOf[EditDocumentActivity])
    intent.putExtra("documentId", document.id)
    startActivity(intent)
    true
  }
}

private class FileActionClickListener(val activity: Activity, val path: String, val mime: String, val downloadFileDialog: DownloadFileDialog) extends View.OnClickListener {
  override def onClick(v: View): Unit = {
    val intent: Intent = new Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(Uri.parse("file://" + path), mime)
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
      activity.startActivity(intent)
    }
    catch {
      case e: ActivityNotFoundException => {
        val builder: AlertDialog.Builder = new AlertDialog.Builder(activity)
        builder.setMessage("You do not have application to open this file. Do you want download this file to Download directory?").setPositiveButton("Yes", downloadFileDialog).setNegativeButton("No", downloadFileDialog).show
      }
    }
  }
}

private class DownloadFileDialog(val documentId: Int, val storeFilename: String, val filename: String) extends DialogInterface.OnClickListener {
  private lazy val fileDao: FileDao = wire(classOf[FileDao])
  private lazy val messageMaker: MessageMaker = wire(classOf[MessageMaker])

  def onClick(dialog: DialogInterface, which: Int) {
    which match {
      case DialogInterface.BUTTON_POSITIVE =>
        // findFirstFreeName(filename) TODO: change store filename
        fileDao.copyFileToDownloadDir(documentId, storeFilename, filename)
        messageMaker.info("File was copied to download directory")
      case DialogInterface.BUTTON_NEGATIVE =>
    }
  }

  private def findFirstFreeName(filename: String): String = {
    var filenameToSave = filename
    while (fileDao.fileExistsInDownloadDir(filename)) {
      val i = filenameToSave.lastIndexOf('.')
    }

    filenameToSave
  }
}
