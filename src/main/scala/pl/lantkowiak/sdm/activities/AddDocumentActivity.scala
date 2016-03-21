package pl.lantkowiak.sdm.activities

import java.io.File
import java.util.{Arrays, Calendar}

import android.os.Bundle
import android.view.{Menu, MenuItem}
import android.webkit.MimeTypeMap
import android.widget.EditText
import org.joda.time.DateTime
import pl.lantkowiak.sdm.R
import pl.lantkowiak.sdm.core.entity.{DocumentTag, Document, DocumentFile, Tag}
import pl.lantkowiak.simpledocumentmanager.model.Document
import pl.lantkowiak.simpledocumentmanager.model.DocumentFile
import pl.lantkowiak.simpledocumentmanager.model.DocumentTag
import pl.lantkowiak.simpledocumentmanager.model.Tag

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

    val now = Calendar.getInstance().getTime

    val document: Document = new Document
    document.title = title
    document.createDate = now

    documentDao.persist(document)

    val tagSet: Set[String] = unpreparedTags.split(" ").toSet
    for (name <- tagSet) {
      val tag: Tag = tagDao.createIfNotExistsAndGetTagByName(name)
      persistDocumentTag(document, tag)
    }

    fileDao.storeFiles(document.id, files)

    var index: Int = 1
    import scala.collection.JavaConversions._
    for (entry <- files.entrySet) {
      val desc: String = getDescriptionForFile(entry.getKey)
      persistDocumentFile(document, ({
        index += 1;
        index - 1
      }), entry.getKey, entry.getValue.getPath, desc)
    }
    import scala.collection.JavaConversions._
    for (file <- files.values) {
      file.delete
    }

    finish()

    true
  }

  private def persistDocumentTag(document: Document, tag: Tag) {
    val documentTag: DocumentTag = new DocumentTag
    documentTag.document = document
    documentTag.tag = tag
    documentTagDao.persist(documentTag)
  }

  protected def persistDocumentFile(document: Document, order: Int, fileId: Long, path: String, description: String) {
    val extension: String = MimeTypeMap.getFileExtensionFromUrl(path)
    val mime: String = MimeTypeMap.getSingleton.getMimeTypeFromExtension(extension)
    val documentFile: DocumentFile = new DocumentFile
    documentFile.setDocument(document)
    documentFile.setOrder(order)
    documentFile.setFileId(fileId)
    documentFile.setExtension(extension)
    documentFile.setMime(mime)
    documentFile.setDescription(description)
    documentFileDao.persist(documentFile)
  }
}
