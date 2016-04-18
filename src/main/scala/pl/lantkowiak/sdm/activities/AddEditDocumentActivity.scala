package pl.lantkowiak.sdm.activities

import java.io.File
import java.util.Date

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.{Bundle, Environment}
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.{MATCH_PARENT, WRAP_CONTENT}
import android.webkit.MimeTypeMap
import android.widget._
import com.getbase.floatingactionbutton.FloatingActionsMenu
import pl.lantkowiak.sdm.core.dao._
import pl.lantkowiak.sdm.core.entity.{Document, DocumentFile}
import pl.lantkowiak.sdm.di.ApplicationModule.wire
import pl.lantkowiak.sdm.gui.{FileChooser, FileSelectedListener}
import pl.lantkowiak.sdm.helper.{MessageMaker, ThumbnailGetter}

import scala.collection.mutable

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
abstract class AddEditDocumentActivity extends AppCompatActivity {
  protected val takePhotoRequestCode = 1432


  protected var fileId: Int = _

  protected var tempFile: File = _

  protected var images: TableLayout = _

  protected lazy val thumbnailGetter = wire(classOf[ThumbnailGetter])
  protected lazy val messageMaker = wire(classOf[MessageMaker])
  protected lazy val documentDao = wire(classOf[DocumentDao])
  protected lazy val documentTagDao = wire(classOf[DocumentTagDao])
  protected lazy val tagDao = wire(classOf[TagDao])
  protected lazy val fileDao = wire(classOf[FileDao])
  protected lazy val documentFileDao = wire(classOf[DocumentFileDao])
  protected lazy val settingDao = wire(classOf[SettingDao])

  protected val files = new mutable.LinkedHashMap[Int, File]

  protected def getImages: Int

  protected def getContentView: Int

  protected def getAddMenu: Int

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(getContentView)
    images = findViewById(getImages).asInstanceOf[TableLayout]
  }

  /**
   * Method call is defined in XML
   */
  def takePhotoOnClickAction(view: View): Unit = {
    val intent: Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    val tempUri: Uri = getOutputMediaFileUri
    intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri)

    tempFile = new File(tempUri.getPath)

    startActivityForResult(intent, takePhotoRequestCode)

    collapseAddMenu()
  }

  /**
   * Method call is defined in XML
   */
  def choseFileOnClickAction(view: View) {
    val fileSelectedListener = new FileSelectedListener() {
      def fileSelected(file: File) {
        val thumbnail: Bitmap = thumbnailGetter.getThumbnailForFile(file)
        fileId = System.currentTimeMillis.toInt
        files.put(fileId, file)
        images.addView(createTableRowWithPhoto(thumbnail, fileId, file.getName))
      }
    }
    new FileChooser(this, fileSelectedListener, settingDao.getMaxFileSize).showDialog()
    collapseAddMenu()
  }

  protected override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    if (requestCode == takePhotoRequestCode && resultCode == RESULT_OK) {
      val thumbnail: Bitmap = thumbnailGetter.getThumbnailForFile(this.tempFile)
      files.put(fileId, tempFile)
      images.addView(createTableRowWithPhoto(thumbnail, fileId, ""))
    }
  }

  protected def createTableRowWithPhoto(bitmap: Bitmap, fileId: Int, description: String): TableRow = {
    val tr: TableRow = new TableRow(this)
    tr.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    tr.addView(createImageViewWithPhoto(bitmap))
    tr.addView(createEditTextForDescription(fileId, description))
    tr.addView(createDeletePhotoButton(fileId, tr))
    tr
  }

  private def createEditTextForDescription(fileId: Int, description: String): EditText = {
    val editText: EditText = new EditText(this)
    editText.setId(fileId)
    editText.setText(description)
    editText.setWidth(200)
    editText
  }

  private def createImageViewWithPhoto(bitmap: Bitmap): ImageView = {
    val takenPhoto: ImageView = new ImageView(this)
    takenPhoto.setImageBitmap(bitmap)
    takenPhoto
  }

  private def createDeletePhotoButton(fileId: Int, tr: TableRow): ImageView = {
    val deletePhoto: ImageView = new ImageView(this)
    deletePhoto.setImageResource(getResources.getIdentifier("@android:drawable/ic_delete", null, null))
    deletePhoto.setOnClickListener(new View.OnClickListener() {
      def onClick(v: View) {
        removePhoto(fileId, tr)
      }
    })
    deletePhoto
  }

  private def removePhoto(fileId: Int, tr: TableRow) {
    files.remove(fileId)
    images.removeView(tr)
    messageMaker.info("Photo was removed")
  }

  private def getOutputMediaFileUri: Uri = {
    Uri.fromFile(getOutputMediaFile)
  }

  private def getOutputMediaFile: File = {
    val mediaStorageDir: File = new File(Environment.getExternalStorageDirectory, ".temp")
    if (!mediaStorageDir.exists) {
      if (!mediaStorageDir.mkdirs) {
        Log.e(".temp", "failed to create directory")
        return null
      }
    }
    fileId = System.currentTimeMillis.toInt
    new File(mediaStorageDir, File.separator + fileId + ".jpg")
  }

  private def collapseAddMenu() {
    findViewById(getAddMenu).asInstanceOf[FloatingActionsMenu].collapse()
  }

  protected def getDescriptionForFile(fileId: Int): String = {
    val descriptionField: View = findViewById(fileId)
    descriptionField.asInstanceOf[EditText].getText.toString.trim
  }

  protected def persistDocumentFile(document: Document, fileId: Int, filename: String, path: String, description: String, createDate: Date) {
    val extension: String = MimeTypeMap.getFileExtensionFromUrl(path)
    val mime: String = MimeTypeMap.getSingleton.getMimeTypeFromExtension(extension)
    val documentFile: DocumentFile = new DocumentFile
    documentFile.document = document
    documentFile.fileId = fileId
    documentFile.filename = filename
    documentFile.extension = extension
    documentFile.mime = mime
    documentFile.description = description
    documentFile.createDate = createDate

    documentFileDao.persist(documentFile)
  }
}
