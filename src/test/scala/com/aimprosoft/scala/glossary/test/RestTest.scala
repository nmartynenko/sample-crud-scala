package com.aimprosoft.scala.glossary.test

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers._
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext

@RunWith(classOf[SpringJUnit4ClassRunner])
@WebAppConfiguration
@ContextConfiguration(locations = Array("classpath:spring/spring-common.xml",//ignore security
  "classpath:spring/spring-db.xml",
  "classpath:servlet/glossary-servlet.xml"))
//TODO use ScalaTest
class RestTest {

  @Autowired
  private var wac: WebApplicationContext = null

  @Autowired
  private var objectMapper: ObjectMapper = null

  private var mockMvc: MockMvc = null

  @Before
  def setup() {
    mockMvc = webAppContextSetup(this.wac).build()
  }

  @Test
  def `get glossaries list`() {
    mockMvc
      //call glossaries list without parameters
      .perform(get("/glossaries").contentType(MediaType.APPLICATION_JSON))
      //expect result is valid
      .andExpect(status().isOk)
      //expect JSON output
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      //there should be content
      .andExpect(jsonPath("$.content").isArray)
      //there should be 3 objects
      .andExpect(jsonPath("$.content").value(hasSize(3)))
      //number of elements should be also 3
      .andExpect(jsonPath("$.numberOfElements", is(3)))
      //and total size should be also 3
      .andExpect(jsonPath("$.totalElements", is(3)))
  }
}
