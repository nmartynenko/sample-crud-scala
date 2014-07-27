package com.aimprosoft.scala.contrib.spring.oval

import net.sf.oval.Validator
import net.sf.oval.configuration.annotation.AnnotationsConfigurer
import net.sf.oval.integration.spring.SpringCheckInitializationListener
import org.springframework.beans.factory.FactoryBean

class CheckInjectableValidatorFactoryBean extends FactoryBean[Validator]{

  def getObject: Validator = {
    val annotationsConfigurer = new AnnotationsConfigurer

    annotationsConfigurer addCheckInitializationListener SpringCheckInitializationListener.INSTANCE

    new Validator(annotationsConfigurer)
  }

  def getObjectType: Class[_] = classOf[Validator]

  def isSingleton: Boolean = false

}