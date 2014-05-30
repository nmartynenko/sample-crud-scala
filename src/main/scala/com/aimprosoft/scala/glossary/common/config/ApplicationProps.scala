package com.aimprosoft.scala.glossary.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * This class provides application properties on JSP pages
 */
@Service("props")
class ApplicationProps {

  @Value("${paginator.default.pageSize}")
  val defaultPaginatorPageSize: Int = 0

  @Value("${paginator.default.maxPage}")
  val defaultPaginatorMaxPage: Int = 0

  @Value("${paginator.default.fastStep}")
  val defaultPaginatorFastStep: Int = 0
}