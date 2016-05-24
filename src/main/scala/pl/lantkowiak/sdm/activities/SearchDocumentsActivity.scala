package pl.lantkowiak.sdm.activities

import java.util.concurrent.{TimeUnit, Executors}

import android.app.{DialogFragment, DatePickerDialog, Dialog, Activity}
import android.app.Activity.RESULT_OK
import android.content.{IntentSender, Intent}
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.{EditText, DatePicker}
import com.google.android.gms.common.{GooglePlayServicesUtil, ConnectionResult}
import com.google.android.gms.common.api.{PendingResult, ResultCallback, GoogleApiClient}
import com.google.android.gms.common.api.GoogleApiClient.{OnConnectionFailedListener, ConnectionCallbacks}
import com.google.android.gms.drive.{DriveApi, Drive}
import com.google.android.gms.drive.DriveApi.MetadataBufferResult
import com.google.android.gms.drive.query.{SearchableField, Filters, Query}
import pl.lantkowiak.sdm.R

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class SearchDocumentsActivity extends DrawerMenuActivity {
  private val datePickerCode = 1253;

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

  }

  override def onStart() {
    super.onStart()

  }

  override def positionInMenu: Int = 1

  override def contentId: Int = R.layout.activity_search_documents

  def datePicker(view: View): Unit = {
    Log.e("as", view.getId.toString)
    showDialog(datePickerCode)
    val fragment: DialogFragment = new DialogFragment()
  }

  override protected def onCreateDialog(id: Int): Dialog = {
    if (id == datePickerCode) {
      return new DatePickerDialog(this, new DateListener(findViewById(R.id.from_date).asInstanceOf[EditText]), 2015, 11, 12)
    }
    null
  }
}

class DateListener(editText: EditText) extends DatePickerDialog.OnDateSetListener {
  override def onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int): Unit = {
    editText.setText(year + "-" + monthOfYear + "-" + dayOfMonth)
  }
}