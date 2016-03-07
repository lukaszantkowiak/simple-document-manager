package pl.lantkowiak.sdm.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.{ActionBarDrawerToggle, AppCompatActivity}
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import pl.lantkowiak.sdm.R

/**
 * Base class for each activity which has DrawerMenu
 *
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
abstract class DrawerMenuActivity extends AppCompatActivity with NavigationView.OnNavigationItemSelectedListener {

  def positionInMenu: Int

  def contentId: Int

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    setContentView(contentId)

    val toolbar = findViewById(R.id.toolbar).asInstanceOf[Toolbar]
    setSupportActionBar(toolbar)
    initDrawerMenu(toolbar)
    initNavigationView()
  }

  override def onBackPressed(): Unit = {
    val drawer = findViewById(R.id.drawer_layout).asInstanceOf[DrawerLayout]
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override def onNavigationItemSelected(item: MenuItem): Boolean = {
    val id = item.getItemId

    val clazz = id match {
      case R.id.left_menu_recent_documents => classOf[ViewRecentDocumentsActivity]
      case R.id.left_menu_search => classOf[SearchDocumentsActivity]
    }

    if (clazz != null && clazz != this.getClass) {
      val intent = new Intent(this, clazz)
      startActivity(intent)
    }

    val drawer = findViewById(R.id.drawer_layout).asInstanceOf[DrawerLayout]
    drawer.closeDrawer(GravityCompat.START)

    true
  }

  def initDrawerMenu(toolbar: Toolbar): Unit = {
    val drawer = findViewById(R.id.drawer_layout).asInstanceOf[DrawerLayout]
    val toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer.addDrawerListener(toggle)
    toggle.syncState()
  }

  def initNavigationView() {
    val navigationView: NavigationView = findViewById(R.id.nav_view).asInstanceOf[NavigationView]
    navigationView.setNavigationItemSelectedListener(this)
    navigationView.getMenu.getItem(positionInMenu).setChecked(true)
  }
}
