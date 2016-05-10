package pl.lantkowiak.sdm.activities

import java.util.Calendar

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.DatePicker
import com.getbase.floatingactionbutton.FloatingActionButton
import pl.lantkowiak.sdm.R

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class SearchDocumentsActivity extends DrawerMenuActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    Log.e("sas", "sasa")

    val c = Calendar.getInstance();
    val mYear = c.get(Calendar.YEAR);
    val mMonth = c.get(Calendar.MONTH);
    val mDay = c.get(Calendar.DAY_OF_MONTH);

    val dpd = new DatePickerDialog(this,
      new DatePickerDialog.OnDateSetListener() {

        override def onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) = {

          Log.e("sas", dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)

        }
      }, mYear, mMonth, mDay)
    dpd.show()
  }

  override def positionInMenu: Int = 1

  override def contentId: Int = R.layout.activity_search_documents
}
