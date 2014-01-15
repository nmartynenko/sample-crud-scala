package com.aimprosoft.scala.glossary.common.exception

import scala.beans.BeanProperty

class NoGlossaryFoundException(cause: Throwable, @BeanProperty var modelId: java.lang.Long)
  extends GlossaryException(null, cause)