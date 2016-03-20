package pl.lantkowiak.sdm.activities

import android.content.Intent
import android.os.Bundle
import android.text.{Editable, TextWatcher}
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.{EditText, LinearLayout, TextView}
import pl.lantkowiak.sdm.R
import pl.lantkowiak.sdm.core.dao.DocumentDao
import pl.lantkowiak.sdm.core.entity.Tag
import pl.lantkowiak.sdm.di.ApplicationModule.wire
import pl.lantkowiak.sdm.gui.DocumentItemCreator

/**
 *
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class ViewRecentDocumentsActivity extends DrawerMenuActivity {
  private lazy val documentDao = wire(classOf[DocumentDao])

  private lazy val documentItemCreator = wire(classOf[DocumentItemCreator])

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    loadDocuments()
    initTagsEditText()
  }

  override def onResume(): Unit = {
    super.onResume()
    loadDocuments()
  }

  def loadDocuments(): Unit = {
    val lastDocuments = findViewById(R.id.recent_documents_last_documents).asInstanceOf[LinearLayout]
    lastDocuments.removeAllViews()

    val tags = findViewById(R.id.recent_documents_search_tags).asInstanceOf[TextView].getText.toString

    val documents = if (tags.isEmpty) documentDao.getRecentDocuments else documentDao.getDocumentsByTags(tags.split(" "))

    for (document <- documents) {
      val documentItemView = documentItemCreator.create(document.title, joinTags(document.tags))
      documentItemView.setOnClickListener(new OnClickListener {
        override def onClick(v: View): Unit = {
          val intent = new Intent(ViewRecentDocumentsActivity.this, null) // TODO: Show document activity
          intent.putExtra("documentId", document.id)
          startActivity(intent)
        }
      })

      lastDocuments.addView(documentItemView)
    }
  }

  def joinTags(tags: Seq[Tag]): String = {
    val joinedTags = new StringBuilder()

    tags.foreach(t => joinedTags.append(t.name).append(" "))

    tags.toString().trim
  }

  def loadDocuments(view: View) {
    loadDocuments()
  }

  def initTagsEditText(): Unit = {
    findViewById(R.id.recent_documents_clear_search_tags).setOnClickListener(new View.OnClickListener() {
      def onClick(v: View) {
        findViewById(R.id.recent_documents_search_tags).asInstanceOf[EditText].setText("")
        loadDocuments()
      }
    })

    findViewById(R.id.recent_documents_search_tags).asInstanceOf[EditText].addTextChangedListener(new TextWatcher() {
      def beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
      }

      def onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      }

      def afterTextChanged(s: Editable) {
        if (!s.toString.isEmpty) {
          findViewById(R.id.recent_documents_clear_search_tags).setVisibility(View.VISIBLE)
        }
        else {
          findViewById(R.id.recent_documents_clear_search_tags).setVisibility(View.INVISIBLE)
        }
      }
    })

  }

  def startAddDocumentActivity(view: View) {
    val intent: Intent = new Intent(this, classOf[AddDocumentActivity])
    startActivity(intent)
  }

  override def positionInMenu: Int = 0

  override def contentId: Int = R.layout.activity_recent_documents
}