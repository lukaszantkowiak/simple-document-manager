package pl.lantkowiak.sdm.core.dao

import java.io.File

/**
 * @author Lukasz Antkowiak lukasz.patryk.antkowiak@gmail.com
 */
class FileDaoBean(val root: File) extends FileDao {
  if (!root.exists()) {
    root mkdir()
  }
}
