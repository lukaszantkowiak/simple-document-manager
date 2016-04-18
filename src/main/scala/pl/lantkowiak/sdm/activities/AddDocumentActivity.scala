package pl.lantkowiak.sdm.activities

import java.io.File
import java.util.Calendar

import android.os.Bundle
import android.view.{Menu, MenuItem}
import android.widget.EditText
import pl.lantkowiak.sdm.R
import pl.lantkowiak.sdm.core.entity.{Document, DocumentTag, Tag}

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

    if (title isEmpty()) {
      messageMaker.info("Title cannot be empy")
      return false
    }
    if (unpreparedTags isEmpty()) {
      messageMaker.info("At least one tag has to be added")
      return false
    }
//    if (images.getChildCount == 0) {
//      messageMaker.info("At least one file has to be added")
//      return false
//    }

    val now = Calendar.getInstance().getTime

    val document: Document = new Document
    document.title = title
    document.createDate = now

    documentDao.persist(document)

    val tagSet: Set[String] = unpreparedTags.split(" ").toSet
    tagSet.foreach(t => {
      val tag: Tag = tagDao.createIfNotExistsAndGetTagByName(t)
      persistDocumentTag(document, tag)
    })

    fileDao.storeFiles(document.id, files)

    files.foreach((e: (Int, File)) => {
      persistDocumentFile(document, e._1, e._2.getName, e._2.getPath, getDescriptionForFile(e._1), now)
      e._2.delete()
    })

    finish()

    true
  }

  private def persistDocumentTag(document: Document, tag: Tag) {
    val documentTag: DocumentTag = new DocumentTag
    documentTag.document = document
    documentTag.tag = tag
    documentTagDao.persist(documentTag)
  }
}
