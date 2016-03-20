package pl.lantkowiak.sdm.activities

import java.io.File

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
import android.widget._
import com.getbase.floatingactionbutton.FloatingActionsMenu
import pl.lantkowiak.sdm.core.dao.DocumentDao
import pl.lantkowiak.sdm.di.ApplicationModule.wire
import pl.lantkowiak.sdm.helper.{MessageMaker, ThumbnailGetter}

import scala.collection.mutable

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
abstract class AddEditDocumentActivity extends AppCompatActivity {
  protected val takePhotoRequestCode = 1432


  protected var fileId: String = _

  protected var tempFile: File = _

  protected var images: TableLayout = _

  private lazy val thumbnailGetter = wire(classOf[ThumbnailGetter])
  protected lazy val messageMaker = wire(classOf[MessageMaker])
  protected lazy val documentDao = wire(classOf[DocumentDao])

  protected val files = new mutable.LinkedHashMap[String, File]

  protected def getImages: Int

  protected def getContentView: Int

  protected def getAddMenu: Int

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(getContentView)
    images = findViewById(getImages).asInstanceOf[TableLayout]
  }

  def takePhotoOnClickAction(view: View): Unit = {
    val intent: Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    val tempUri: Uri = getOutputMediaFileUri
    intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri)

    tempFile = new File(tempUri.getPath)

    startActivityForResult(intent, takePhotoRequestCode)

    collapseAddMenu()
  }

  protected override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    if (requestCode == takePhotoRequestCode && resultCode == RESULT_OK) {
      val thumbnail: Bitmap = thumbnailGetter.getThumbnailForFile(this.tempFile)
      files.put(fileId, tempFile)
      images.addView(createTableRowWithPhoto(thumbnail, fileId, ""))
    }
  }

  protected def createTableRowWithPhoto(bitmap: Bitmap, fileId: String, description: String): TableRow = {
    val tr: TableRow = new TableRow(this)
    tr.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    tr.addView(createImageViewWithPhoto(bitmap))
    tr.addView(createEditTextForDescription(fileId, description))
    tr.addView(createDeletePhotoButton(fileId, tr))
    tr
  }

  private def createEditTextForDescription(fileId: String, description: String): EditText = {
    val editText: EditText = new EditText(this)
    editText.setId(fileId.hashCode)
    editText.setText(description)
    editText
  }

  private def createImageViewWithPhoto(bitmap: Bitmap): ImageView = {
    val takenPhoto: ImageView = new ImageView(this)
    takenPhoto.setImageBitmap(bitmap)
    takenPhoto
  }

  private def createDeletePhotoButton(id: String, tr: TableRow): ImageView = {
    val deletePhoto: ImageView = new ImageView(this)
    deletePhoto.setImageResource(getResources.getIdentifier("@android:drawable/ic_delete", null, null))
    deletePhoto.setOnClickListener(new View.OnClickListener() {
      def onClick(v: View) {
        removePhoto(id, tr)
      }
    })
    deletePhoto
  }

  private def removePhoto(id: String, tr: TableRow) {
    files.remove(id)
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
    fileId = System.currentTimeMillis + "" //TODO: change format
    new File(mediaStorageDir, File.separator + fileId + ".jpg")
  }

  private def collapseAddMenu() {
    val fam: FloatingActionsMenu = findViewById(getAddMenu).asInstanceOf[FloatingActionsMenu]
    fam.collapse()
  }

  protected def getDescriptionForFile(fileId: String): String = {
    findViewById(fileId.hashCode).asInstanceOf[EditText].getText.toString
  }
}
