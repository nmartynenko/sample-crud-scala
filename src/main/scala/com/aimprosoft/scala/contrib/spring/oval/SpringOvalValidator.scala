package com.aimprosoft.scala.contrib.spring.oval

import java.lang.{reflect => jreflect}
import java.util

import net.sf.oval.context.FieldContext
import net.sf.oval.exception.ValidationFailedException
import net.sf.oval.{ConstraintViolation, Validator}
import org.springframework.beans.factory.InitializingBean
import org.springframework.util.Assert
import org.springframework.validation.Errors

import scala.language.reflectiveCalls

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

class SpringOvalValidator extends org.springframework.validation.Validator with InitializingBean {

  //using such type because of Java&Scala interoperability issues, e.g. ambiguity of methods execution
  private type Validatable = {def validate(o: Object): util.List[ConstraintViolation]}

  var validator: Validator = _

  def supports(clazz: Class[_]) = true

  def validate(target: Object, errors: Errors) {
    doValidate(target, errors, "")
  }

  @SuppressWarnings(Array("unchecked"))
  private def doValidate(target: Object, errors: Errors, fieldPrefix: String) {
    try {
      //validation of current object
      val constraintViolations = validator.asInstanceOf[Validatable].validate(target)

      for (violation <- constraintViolations.toList) {
        val errorCode = violation.getErrorCode
        val errorMessage = violation.getMessage

        violation.getContext match {
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
        val validate = field getAnnotation classOf[SpringValidateNestedProperty]
        if (validate != null) {
          if (!field.isAccessible) {
            field.setAccessible(true)
          }

          val nestedProperty = field.get(target)

          if (nestedProperty != null) {
            val name = field.getName
            
            //specify method helpers
            //for collections and arrays
            def handleSequences(objects: Seq[_]) {
              var index = 0
              for (o <- objects) {
                doValidate(o.asInstanceOf[Object], errors, name + "[" + index + "].")
                index = index + 1
              }
            }

            //for maps
            def handleMap(m: Map[_, _]) {
              for ((key, value) <- m) {
                key match {
                  case k: String =>
                    doValidate(k, errors, name + "['" + k + "']")
                  case _ =>
                    throw new IllegalArgumentException("Map as a nested property supports only String keys for validation")
                }
              }
            }

            //NOTE: this is not well tested with Scala case classes
            nestedProperty match {
              //valueToValidate is a collection or Scala Array
              case c: util.Collection[_] =>
                handleSequences(c.toList)
              case c: Seq[_] =>
                handleSequences(c)

              //valueToValidate is a map
              case m: util.Map[_, _] =>
                handleMap(m.toMap)
              case m: Map[_, _] =>
                handleMap(m)

              //valueToValidate is an Java array
              case _ if nestedProperty.getClass.isArray =>
                val length = jreflect.Array.getLength(nestedProperty)
                for (i <- 0 until length) {
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

  private def getFields(target: Object) = {
    def doGetFields(clazz: Class[_]): List[jreflect.Field] = {
      clazz match {
        case null =>
          List()
        case c: Class[_] =>
          clazz.getDeclaredFields.toList ::: doGetFields(c.getSuperclass)
      }
    }

    doGetFields(target.getClass)
  }

  @throws[Exception]
  def afterPropertiesSet() {
    Assert.notNull(validator, "Property [validator] is not set")
  }

}