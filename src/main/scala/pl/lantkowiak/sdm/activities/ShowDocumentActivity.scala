package pl.lantkowiak.sdm.activities

import java.io.File

import android.content.{ActivityNotFoundException, DialogInterface, Intent}
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.{AlertDialog, AppCompatActivity}
import android.view.{Menu, MenuItem, View}
import android.widget.{ImageView, LinearLayout, TextView}
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

  def loadDocument() = {
    loadTitle()
    loadTags()
    loadFiles()
  }

  private def loadTitle() {
    setTitle(document.title)
  }

  private def loadTags() {
    val sb = new StringBuilder

    val tags: List[Tag] = tagDao.getTagsById(document.documentTags.map(t => t.tag.id).toList)
    tags.foreach(t => sb.append(t.name).append(" "))

    findViewById(R.id.show_document_tags).asInstanceOf[TextView].setText(sb.toString())
  }

  private def loadFiles() {
    val filesLayout: LinearLayout = findViewById(R.id.show_document_files).asInstanceOf[LinearLayout]
    filesLayout.removeAllViews()
    val documentFiles: List[DocumentFile] = documentFileDao.getFilesByDocumentId(document.id)
    for (documentFile <- documentFiles) {
      val imageView: ImageView = new ImageView(this)
      val file: File = fileDao.getFile(document.id, documentFile.fullFilename)
      val thumbnail: Bitmap = thumbnailGetter.getThumbnailForFile(file)
      imageView.setImageBitmap(thumbnail)
      imageView.setOnClickListener(new View.OnClickListener() {
        def onClick(v: View) {
          val intent: Intent = new Intent(Intent.ACTION_VIEW)
          intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath), documentFile.mime)
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          try {
            startActivity(intent)
          }
          catch {
            case e: ActivityNotFoundException => {
              val downloadFileDialog = new DownloadFileDialog(document.id, documentFile.fullFilename)
              val builder: AlertDialog.Builder = new AlertDialog.Builder(ShowDocumentActivity.this)
              builder.setMessage("You do not have application to open this file. Do you want download this file to Download directory?").setPositiveButton("Yes", downloadFileDialog).setNegativeButton("No", downloadFileDialog).show
            }
          }
        }
      })
      filesLayout.addView(imageView)
    }
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

private class DownloadFileDialog(val documentId: Int, val filename: String) extends DialogInterface.OnClickListener {
  private lazy val fileDao: FileDao = wire(classOf[FileDao])
  private lazy val messageMaker: MessageMaker = wire(classOf[MessageMaker])

  def onClick(dialog: DialogInterface, which: Int) {
    which match {
      case DialogInterface.BUTTON_POSITIVE =>
        fileDao.copyFileToDownloadDir(documentId, filename)
        messageMaker.info("File was copied to download directory")
    }
  }
}
