package com.aimprosoft.scala.glossary.common.exception

import com.aimprosoft.scala.glossary.ApplicationException

class GlossaryException(message: String, var cause: Throwable)
  extends ApplicationException(message, cause) {
}
