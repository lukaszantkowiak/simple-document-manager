package pl.lantkowiak.sdm.helper

import android.content.Context
import android.widget.Toast

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class MessageMaker(val context: Context) {
  def info(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
  }
}
