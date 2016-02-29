package pl.lantkowiak.sdm

import android.os.Bundle
import android.support.design.widget.{Snackbar, FloatingActionButton}
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View.OnClickListener
import android.view.{View, Menu, MenuItem}
import pl.lantkowiak.sdm.R

class MainActivity extends AppCompatActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val toolbar = findViewById(R.id.toolbar).asInstanceOf[Toolbar]
    setSupportActionBar(toolbar)

    val fab = findViewById(R.id.fab).asInstanceOf[FloatingActionButton]
    val clickListener = new OnClickListener {
      override def onClick(view: View): Unit = {
        Snackbar.make(view, "Replace with your own action.", Snackbar.LENGTH_LONG).setAction("Action", null).show()
      }
    }

    fab.setOnClickListener(clickListener)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater().inflate(R.menu.menu_main, menu)

    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    val id = item.getItemId()

    if (id == R.id.action_settings) {
      return true
    }

    super.onOptionsItemSelected(item)
  }
}