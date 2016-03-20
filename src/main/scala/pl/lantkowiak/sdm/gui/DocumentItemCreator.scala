package pl.lantkowiak.sdm.gui

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.{WRAP_CONTENT, MATCH_PARENT}
import android.view.{View, ViewGroup}
import android.widget.{TableLayout, TextView, LinearLayout}
import android.widget.LinearLayout.LayoutParams

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class DocumentItemCreator(val context: Context) {
  def create(title: String, tags: String): View = {
    val documentLayout = new LinearLayout(this.context)
    val layoutParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT)
    layoutParams.setMargins(0, 10, 0, 10)
    documentLayout.setLayoutParams(layoutParams)
    documentLayout.setBackgroundColor(Color.parseColor("#f1f1f1"))
    documentLayout.setOrientation(LinearLayout.VERTICAL)


    val titleTextView = new TextView(this.context)
    titleTextView.setText(title)
    titleTextView.setTextAppearance(this.context, android.R.style.TextAppearance_Large)
    titleTextView.setLayoutParams(new TableLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 0.1f))
    documentLayout.addView(titleTextView)

    val tagsTextView = new TextView(this.context)
    tagsTextView.setText(tags)
    tagsTextView.setTextAppearance(this.context, android.R.style.TextAppearance_Small)
    tagsTextView.setLayoutParams(new TableLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 0.1f))
    documentLayout.addView(tagsTextView)

    documentLayout
  }
}