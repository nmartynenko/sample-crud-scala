package com.aimprosoft.scala.contrib.spring.validator

import java.lang.{reflect => jreflect}
import java.util
import net.sf.oval.context.FieldContext
import net.sf.oval.exception.ValidationFailedException
import net.sf.oval.{ConstraintViolation, Validator}
import org.springframework.beans.factory.InitializingBean
import org.springframework.util.Assert
import org.springframework.validation.Errors
import scala.beans.BeanProperty

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

      //val constraintViolations = validator.validate(target)

      //using Java reflection because of Java&Scala interoperability issues
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

            //NOTE: this is not well tested with Scala case classes
            nestedProperty match {
              //valueToValidate is a collection
              case c: util.Collection[_] =>
                var index = 0
                for (o <- c.toList) {
                  doValidate(o.asInstanceOf[Object], errors, name + "[" + index + "].")
                  index = index + 1
                }

              //valueToValidate is a collection
              case m: util.Map[_, _] =>
                for ((key, value) <- m.toMap){
                  key match {
                    case k: String =>
                      doValidate(k, errors, name + "['" + k + "']")
                    case _ =>
                      throw new IllegalArgumentException("Map as a nested property supports only String keys for validation")
                  }
                }

              //valueToValidate is an array
              case _ if nestedProperty.getClass.isArray =>
                val length = jreflect.Array.getLength(nestedProperty)
                for (i <- 0 to length) {
                  val o = jreflect.Array.get(nestedProperty, i)
                  doValidate(o, errors, name + "[" + i + "].")
                }

              //valueToValidate is other object
              case _ =>
                doValidate(nestedProperty, errors, name + ".")
            }
          }
        }
      }

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
  private def doGetFields(clazz: Class[_]): List[jreflect.Field] = {
    clazz match {
      case null =>
        List()
      case c: Class[_] =>
        clazz.getDeclaredFields.toList ::: doGetFields(c.getSuperclass)
    }
  }


  @throws[Exception]
  def afterPropertiesSet() {
    Assert.notNull(validator, "Property [validator] is not set")
  }

}