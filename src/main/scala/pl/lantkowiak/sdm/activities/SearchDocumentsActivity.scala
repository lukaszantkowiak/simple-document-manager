package pl.lantkowiak.sdm.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import com.getbase.floatingactionbutton.FloatingActionButton
import pl.lantkowiak.sdm.R

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class SearchDocumentsActivity extends DrawerMenuActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    Log.e("sas", "sasa")
  }

  override def positionInMenu: Int = 1

  override def contentId: Int = R.layout.activity_search_documents
}
