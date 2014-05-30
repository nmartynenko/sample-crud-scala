package com.aimprosoft.scala.glossary.servlet.controller

import com.typesafe.scalalogging.slf4j.StrictLogging
import java.util.Locale
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.context.request.{ServletRequestAttributes, RequestContextHolder}

trait BaseController extends StrictLogging {

  @Autowired
  protected var messageSource: MessageSource = _

  protected def getRequest: Option[HttpServletRequest] = {
    RequestContextHolder.getRequestAttributes match {
      case attrs: ServletRequestAttributes =>
        Some(attrs.getRequest)
      case _ =>
        None
    }
  }

  protected def getLocale: Locale = {
    LocaleContextHolder.getLocale
  }

  //logs exception and returns it's message
  protected def simpleExceptionHandler(th: Throwable) = {
    logger.error(th.getMessage, th)

    th.getMessage
  }

}
