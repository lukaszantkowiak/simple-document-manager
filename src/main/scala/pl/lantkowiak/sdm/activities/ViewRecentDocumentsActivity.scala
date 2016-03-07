package pl.lantkowiak.sdm.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.View.OnClickListener
import com.getbase.floatingactionbutton.FloatingActionButton
import pl.lantkowiak.sdm.R

/**
 *
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class ViewRecentDocumentsActivity extends DrawerMenuActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    initAddDocumentButton()
  }

  def initAddDocumentButton(): Unit = {
    val fab = findViewById(R.id.add_item_button).asInstanceOf[FloatingActionButton]
    val clickListener = new OnClickListener {
      override def onClick(view: View): Unit = {
        Snackbar.make(view, "You want to add document!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
      }
    }
    fab.setOnClickListener(clickListener)
  }

  override def positionInMenu: Int = 0

  override def contentId: Int = R.layout.activity_recent_documents
}