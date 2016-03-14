package pl.lantkowiak.sdm.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import pl.lantkowiak.sdm.R

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class AddDocumentActivity extends AppCompatActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_add_document)
  }
}
