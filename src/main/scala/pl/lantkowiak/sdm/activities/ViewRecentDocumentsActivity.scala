package pl.lantkowiak.sdm.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.{TextView, LinearLayout}
import pl.lantkowiak.sdm.R
import pl.lantkowiak.sdm.core.dao.DocumentDao
import pl.lantkowiak.sdm.core.entities.Document
import pl.lantkowiak.sdm.di.ApplicationModule

/**
 *
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class ViewRecentDocumentsActivity extends DrawerMenuActivity {
  private lazy val documentDao = ApplicationModule.wire(classOf[DocumentDao])


  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    //    initAddDocumentButton()

    loadDocuments()

  }


  def loadDocuments(): Unit = {
    val lastDocuments = findViewById(R.id.main_last_documents).asInstanceOf[LinearLayout]
    lastDocuments.removeAllViews()

    val tags = findViewById(R.id.main_search_tags).asInstanceOf[TextView].getText

    val recentDocuments: List[Document] = documentDao.getRecentDocuments
  }

  def loadDocuments(view: View) {
    loadDocuments()
  }

  //  def initAddDocumentButton(): Unit = {
  //    val fab = findViewById(R.id.add_item_button).asInstanceOf[FloatingActionButton]
  //    val clickListener = new OnClickListener {
  //      override def onClick(view: View): Unit = {
  //        Snackbar.make(view, "You want to add document!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
  //      }
  //    }
  //    fab.setOnClickListener(clickListener)
  //  }

  def startAddDocumentActivity(view: View) {
    Log.e("dsaasds", "startAddDocumentActivity")
    val intent: Intent = new Intent(this, classOf[AddDocumentActivity])
    startActivity(intent)
  }

  override def positionInMenu: Int = 0

  override def contentId: Int = R.layout.activity_recent_documents
}