package com.aimprosoft.scala.glossary.servlet.controller

import com.aimprosoft.scala.glossary.common.exception.{GlossaryException, NoGlossaryFoundException}
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.{FieldError, Errors}
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.{ResponseStatus, ExceptionHandler, ResponseBody, ControllerAdvice}
import scala.collection.mutable

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

@ControllerAdvice
class GlossaryControllerAdvice extends BaseController {

  //EXCEPTION HANDLERS
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = Array(classOf[NoGlossaryFoundException]))
  @ResponseBody
  def handleNoGlossaryFoundException(e: NoGlossaryFoundException) = {
    messageSource.getMessage("sample.error.glossary.not.found",
      Array(e.getModelId), getLocale)
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = Array(classOf[GlossaryException]))
  @ResponseBody
  def handleGlossaryException(e: GlossaryException) = {
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
    val errorsMap = mutable.Map[String, String]()

    val allErrors = errors.getAllErrors.toList

    for (error <- allErrors) {
      val objectName = error match {
        case error: FieldError =>
          error.getField
        case _ =>
          error.getObjectName
      }

      errorsMap.put(objectName,
        messageSource.getMessage(error.getDefaultMessage, Array(objectName), getLocale))
    }

    errorsMap
  }
}
