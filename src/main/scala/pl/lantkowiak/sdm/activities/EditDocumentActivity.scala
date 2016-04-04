package pl.lantkowiak.sdm.activities

import java.io.File
import java.util.Calendar

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.widget.{TextView, EditText}
import com.google.common.base.Joiner
import pl.lantkowiak.sdm.R
import pl.lantkowiak.sdm.core.entity.{DocumentTag, Tag, Document, DocumentFile}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions._


/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class EditDocumentActivity extends AddEditDocumentActivity {
  private var documentId: Int = _

  override protected def getImages: Int = R.id.edit_document_images

  override protected def getContentView: Int = R.layout.activity_edit_document

  override protected def getAddMenu: Int = R.id.edit_document_add_menu

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    documentId = getIntent.getIntExtra("documentId", -1)
    loadDocument()
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    val id: Int = item.getItemId
    if (id == R.id.edit_document_save_action) {
      return updateDocument()
    }
    super.onOptionsItemSelected(item)
  }

  private def loadDocument() {
    val document: Document = documentDao.getDocumentById(documentId)
    val tagIds: List[Int] = document.documentTags.map(t => t.tag.id).toList
    val tags: List[Tag] = tagDao.getTagsByIds(tagIds)
    val documentFiles: List[DocumentFile] = documentFileDao.getFilesByDocumentId(documentId)
    val files: Iterable[File] = fileDao.getFiles(documentFiles)
    loadTitle(document.title)
    loadTags(tags)
    loadFiles(documentFiles, files)
  }

  private def loadTitle(title: String) {
    findViewById(R.id.edit_document_title).asInstanceOf[TextView].setText(title)
  }

  private def loadTags(tags: List[Tag]) {
    val preparedTags: String = Joiner.on(' ').join(tags.map(t => t.name))
    findViewById(R.id.edit_document_tags).asInstanceOf[TextView].setText(preparedTags)
  }

  private def loadFiles(documentFiles: Iterable[DocumentFile], files: Iterable[File]) {
    {
      for ((df, f) <- documentFiles zip files) {
        val thumbnail: Bitmap = thumbnailGetter.getThumbnailForFile(f)

        val fileId: String = df.filename
        this.files.put(fileId, f)

        images.addView(createTableRowWithPhoto(thumbnail, fileId, df.description))
      }
    }
  }

  private def updateDocument(): Boolean = {
    val tags: String = findViewById(R.id.edit_document_tags).asInstanceOf[EditText].getText.toString
    val title: String = findViewById(R.id.edit_document_title).asInstanceOf[EditText].getText.toString.trim
    if (title.isEmpty) {
      messageMaker.info("Title has to be added")
      return false
    }
    if (tags.isEmpty) {
      messageMaker.info("At least one tag has to be added")
      return false
    }
    if (images.getChildCount == 0) {
      messageMaker.info("At least one photo has to be added")
      return false
    }
    val document: Document = documentDao.getDocumentById(documentId)
    updateTitle(title, document)
    updateTags(tags, document)
    updateFiles(document)
    finish()
    true
  }

  private def updateTitle(title: String, document: Document) {
    if (document.title != title) {
      document.title = title
      documentDao.update(document)
    }
  }

  private def updateFiles(document: Document) {
    val documentFiles: List[DocumentFile] = documentFileDao.getFilesByDocumentId(document.id)
    val existingFiles = new ArrayBuffer[String]
    val toRemove = new ArrayBuffer[DocumentFile]
    val toUpdate = new ArrayBuffer[DocumentFile]
    for (documentFile <- documentFiles) {
      if (!files.contains(documentFile.filename)) {
        toRemove += documentFile
      }
      else {
        existingFiles += documentFile.filename
        toUpdate += documentFile
      }
    }

    documentFiles.foreach(df =>
      if (!files.contains(df.filename)) {
        toRemove += df
      } else {
        existingFiles += df.filename
        toUpdate += df
      })

    val fileIds = List(files.keySet)
    documentFileDao.remove(toRemove)
    fileDao.remove(toRemove)
    toUpdate.foreach(tu => {
      val desc: String = getDescriptionForFile(tu.filename)
      val order: Int = fileIds.indexOf(tu.filename)
      if (!tu.description.equals(desc)) {
        tu.description = desc
        documentFileDao.update(tu)
      }
    })

    files.foreach((e: (String, File)) => {
      if (!existingFiles.contains(e._1)) {
        fileDao.storeFile(document.id, e._1, e._2)
        val order: Int = fileIds.indexOf(e._1)
        val desc: String = getDescriptionForFile(e._1)
        persistDocumentFile(document, e._1, e._2.getPath, desc, Calendar.getInstance().getTime)
        e._2.delete
      }
    })
  }

  private def updateTags(tags: String, document: Document) {
    val editedTags: Set[String] = tags.split(" ").toSet
    val tagIds: List[Int] = document.documentTags.map(t => t.tag.id).toList
    val tagSet: mutable.LinkedHashSet[String] = new mutable.LinkedHashSet[String]()
    tagDao.getTagsByIds(tagIds).foreach(t => tagSet.add(t.name))

    val newTags: mutable.LinkedHashSet[String] = new mutable.LinkedHashSet[String]()
    newTags ++= editedTags
    newTags --= tagSet
    newTags.foreach(t => {
      val tag: Tag = tagDao.createIfNotExistsAndGetTagByName(t)
      persistDocumentTag(document, tag)
    })

    val removedTags: mutable.LinkedHashSet[String] = new mutable.LinkedHashSet[String]()
    removedTags ++= tagSet
    removedTags --= editedTags
    documentTagDao.removeByDocumentIdAndTagNames(document, tagDao.getTagsByNames(removedTags))
  }

  private def persistDocumentTag(document: Document, tag: Tag) {
    val documentTag: DocumentTag = new DocumentTag()
    documentTag.document = document
    documentTag.tag = tag
    documentTagDao.persist(documentTag)
  }
}
