package com.aimprosoft.scala.glossary.servlet.controller

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.{Errors, FieldError}
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation._

@ControllerAdvice
class GlossaryControllerAdvice extends BaseController {

  //EXCEPTION HANDLERS
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = Array(classOf[DataAccessException]))
  @ResponseBody
  def handleDataAccessException(e: DataAccessException) = {
    simpleExceptionHandler(e)
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(value = Array(classOf[AccessDeniedException]))
  @ResponseBody
  def handleAccessDeniedException(e: AccessDeniedException) {
    simpleExceptionHandler(e)
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(value = Array(classOf[AuthenticationException]))
  @ResponseBody
  def handleAuthenticationException(e: AuthenticationException) = {
    simpleExceptionHandler(e)
  }

  //do not pass validation
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = Array(classOf[MethodArgumentNotValidException]))
  @ResponseBody
  def handleMethodArgumentNotValidException(e: MethodArgumentNotValidException) = {
    transformErrors(e.getBindingResult)
  }

  private def transformErrors(errors: Errors) = {
    //Java2Scala conversions and vice versa
    import scala.collection.JavaConversions._

    (errors.getAllErrors map {error =>
      val objectName = error match {
        case error: FieldError =>
          error.getField
        case _ =>
          error.getObjectName
      }

      val message = messageSource.getMessage(error.getDefaultMessage, Array(objectName),
        getLocale)

      //return tuple, set of which naturally transforms into Map
      (objectName, message)
    }).toMap
  }
}
