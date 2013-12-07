package com.aimprosoft.scala.contrib.jackson

import org.springframework.beans.factory.InitializingBean
import com.fasterxml.jackson.databind.ObjectMapper
import scala.beans.BeanProperty
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.util.Assert

class ScalaJacksonConfigurer extends InitializingBean{

  @BeanProperty
  var objectMapper: ObjectMapper = _

  def afterPropertiesSet(): Unit = {
    Assert.notNull(objectMapper, "Property [objectMapper] is not set")
    objectMapper.registerModule(DefaultScalaModule)
  }
}
