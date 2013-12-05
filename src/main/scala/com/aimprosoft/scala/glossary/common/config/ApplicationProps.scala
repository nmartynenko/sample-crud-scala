package com.aimprosoft.scala.glossary.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import scala.beans.BeanProperty

/**
 * This class provides application properties on JSP pages
 */
@Service("props")
class ApplicationProps {

  @BeanProperty
  @Value("${paginator.default.pageSize}")
  val defaulPaginatorPageSize: Int = 0

  @BeanProperty
  @Value("${paginator.default.maxPage}")
  val defaulPaginatorMaxPage: Int = 0

  @BeanProperty
  @Value("${paginator.default.fastStep}")
  val defaulPaginatorFastStep: Int = 0
}