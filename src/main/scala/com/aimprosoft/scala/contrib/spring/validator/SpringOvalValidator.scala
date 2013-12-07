package com.aimprosoft.scala.contrib.spring.validator

import net.sf.oval.{ConstraintViolation, Validator}
import net.sf.oval.context.FieldContext
import net.sf.oval.exception.ValidationFailedException
import org.springframework.beans.factory.InitializingBean
import org.springframework.util.Assert
import org.springframework.validation.Errors
import scala.beans.BeanProperty
import java.util
import java.lang.reflect.Field

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

class SpringOvalValidator extends org.springframework.validation.Validator with InitializingBean {

  @BeanProperty
  var validator: Validator = _

  def SpringOvalValidator() {
    validator = new Validator()
  }

  def SpringOvalValidator(validator: Validator) {
    this.validator = validator
  }

  @SuppressWarnings(Array("unchecked"))
  def supports(clazz: Class[_]) = {
    true
  }

  def validate(target: Object, errors: Errors) {
    doValidate(target, errors, "")
  }

  @SuppressWarnings(Array("unchecked"))
  private def doValidate(target: Object, errors: Errors, fieldPrefix: String) {
    try {
      //validation of current object
//      val constraintViolations = validator.validate(target)
      val constraintViolations = classOf[Validator].getMethod("validate",
        classOf[Object]).invoke(validator, target).asInstanceOf[util.List[ConstraintViolation]]

      for (violation <- constraintViolations.toList) {
        val context = violation.getContext
        val errorCode = violation.getErrorCode
        val errorMessage = violation.getMessage

        context match {
          case ctx: FieldContext =>
            val fieldName = fieldPrefix + ctx.getField.getName
            errors.rejectValue(fieldName, errorCode, errorMessage)
          case _ =>
            errors.reject(errorCode, errorMessage)
        }
      }

      //validation of nested objects
//      todo finish
/*
      val fields = getFields(target)
      for (field <- fields) {
        val validate = field.getAnnotation(classOf[SpringValidateNestedProperty])
        if (validate != null) {
          if (!field.isAccessible) {
            field.setAccessible(true)
          }

          val nestedProperty = field.get(target)

          if (nestedProperty != null) {
            val name = field.getName

            nestedProperty match {
              case c: util.Collection[_] =>
                ???
              case c: util.Map[_,_] =>
                ???
              case _ if nestedProperty.getClass.isArray =>
                ???
              case _ =>
                doValidate(nestedProperty, errors, name + ".")
            }
          }
        }
      }
*/

    } catch {
      case ex: ValidationFailedException =>
        errors.reject(ex.getMessage)
    }
  }

  @SuppressWarnings(Array("unchecked"))
  private def getFields(target: Object) = {
    doGetFields(target.getClass)
  }

  @SuppressWarnings(Array("unchecked"))
  private def doGetFields(clazz: Class[_]): List[Field] = {
    val fields = clazz.getDeclaredFields.toList

    clazz.getSuperclass match {
      case null =>
        fields
      case sc: Class[_] =>
        fields ::: doGetFields(sc)
    }
  }


  @throws[Exception]
  def afterPropertiesSet() {
    Assert.notNull(validator, "Property [validator] is not set")
  }

}