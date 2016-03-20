package pl.lantkowiak.sdm.activities

import java.io.File
import java.util.Calendar

import android.os.Bundle
import android.view.{Menu, MenuItem}
import android.widget.EditText
import pl.lantkowiak.sdm.R
import pl.lantkowiak.sdm.core.entity.{DocumentFile, Tag}

import scala.collection.mutable.ListBuffer

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class AddDocumentActivity extends AddEditDocumentActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
  }

  override protected def getImages: Int = R.id.add_document_images

  override protected def getContentView: Int = R.layout.activity_add_document

  override protected def getAddMenu: Int = R.id.add_document_add_menu

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu_add_document, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    val id: Int = item.getItemId
    if (id == R.id.add_document_add_action) {
      return persistDocument()
    }
    super.onOptionsItemSelected(item)
  }

  private def persistDocument(): Boolean = {
    val unpreparedTags = findViewById(R.id.add_document_tags).asInstanceOf[EditText].getText.toString.replaceAll("\\s+", " ")
    val title = findViewById(R.id.add_document_title).asInstanceOf[EditText].getText.toString

    if (unpreparedTags isEmpty()) {
      messageMaker.info("At least one tag has to be added")
      return false
    }
    if (title isEmpty()) {
      messageMaker.info("At least one tag has to be added")
      return false
    }

    //    if (images.getChildCount == 0) {
    //      messageMaker.info("At least one photo has to be added")
    //      return false
    //    }

    val preparedTags: Set[String] = unpreparedTags.split(" ").toSet

    val tags = ListBuffer[Tag]()
    preparedTags.foreach(t => tags.append(new Tag(t)))

    val now = Calendar.getInstance().getTime
    val documentFiles = new ListBuffer[DocumentFile]
    files.foreach((e: (String, File)) => documentFiles.append(new DocumentFile(now, e._1, getDescriptionForFile(e._1))))

    val document = new pl.lantkowiak.sdm.core.entity.Document(title, Calendar.getInstance().getTime, tags)
    document.documentFiles = documentFiles

    documentDao.persist(document)

    true
  }
}
