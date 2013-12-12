package com.aimprosoft.scala.glossary.test

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Before
import org.scalatest.junit.JUnitSuite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders._
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.context.{ContextConfiguration, ContextHierarchy}
import scala.Array

@WebAppConfiguration
@ContextHierarchy(
  Array(
    new ContextConfiguration(
      name = "base",
      locations = Array("classpath:spring/spring-common.xml", //ignore security
        "classpath:spring/spring-db.xml")
    ),
    new ContextConfiguration(
      name = "servlet",
      locations = Array("classpath:servlet/glossary-servlet.xml")
    )
  )
)
abstract class BaseTest extends JUnitSuite {

  @Autowired
  protected val wac: WebApplicationContext = null

  @Autowired
  protected val objectMapper: ObjectMapper = null

  protected var mockMvc: MockMvc = _

  @Before
  def setup() {
    mockMvc = webAppContextSetup(this.wac).build()
  }

}