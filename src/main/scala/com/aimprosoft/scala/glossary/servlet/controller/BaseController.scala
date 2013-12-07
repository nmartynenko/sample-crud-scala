package com.aimprosoft.scala.glossary.servlet.controller

import java.util.Locale
import javax.servlet.http.HttpServletRequest
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.context.request.{ServletRequestAttributes, RequestContextHolder}
import com.fasterxml.jackson.databind.ObjectMapper

trait BaseController {

  protected val _logger: Logger = LoggerFactory.getLogger(getClass)

  @Autowired
  protected var messageSource: MessageSource = _

  @Autowired
  protected var objecMapper: ObjectMapper = _

  protected def getRequest: HttpServletRequest = {
    RequestContextHolder.getRequestAttributes match {
      case attrs: ServletRequestAttributes =>
        attrs.getRequest
      case _ =>
        null
    }
  }

  protected def getLocale: Locale = {
    LocaleContextHolder.getLocale
  }

  //logs exception and returns it's message
  protected def simpleExceptionHandler(th: Throwable) = {
    _logger.error(th.getMessage, th)

    th.getMessage
  }

}
