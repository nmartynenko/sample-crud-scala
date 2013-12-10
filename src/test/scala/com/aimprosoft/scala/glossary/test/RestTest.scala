package com.aimprosoft.scala.glossary.test

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers._
import org.junit.{FixMethodOrder, Before, Test}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitSuite
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
import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import org.junit.runners.MethodSorters

@RunWith(classOf[SpringJUnit4ClassRunner])
@WebAppConfiguration
@ContextConfiguration(locations = Array("classpath:spring/spring-common.xml", //ignore security
  "classpath:spring/spring-db.xml",
  "classpath:servlet/glossary-servlet.xml"))
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RestTest extends JUnitSuite {

  @Autowired
  private val wac: WebApplicationContext = null

  @Autowired
  private val objectMapper: ObjectMapper = null

  private var mockMvc: MockMvc = _

  @Before
  def setup() {
    mockMvc = webAppContextSetup(this.wac).build()
  }

  @Test
  def `01 glossaries list should return all items`() {
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

  @Test
  def `02 glossaries list with pagination`() {
    mockMvc
      //call glossaries list with parameters
      .perform(get("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .param("startRow", "2")
        .param("pageSize", "2")
      )
      //expect result is valid
      .andExpect(status().isOk)
      //expect JSON output
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      //there should be content
      .andExpect(jsonPath("$.content").isArray)
      //there should be 1 objects
      .andExpect(jsonPath("$.content", hasSize(1)))
      //number of elements should be also 1
      .andExpect(jsonPath("$.numberOfElements", is(1)))
      //total number of elements should be 3
      .andExpect(jsonPath("$.totalElements", is(3)))
  }

  @Test
  def `03 glossaries list with wrong pagination`() {
    mockMvc
      //call glossaries list with incorrect parameters
      .perform(get("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .param("startRow", "100500")
        .param("pageSize", "2")
      )
      //expect result is valid
      .andExpect(status().isOk)
      //expect JSON output
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      //there should be content
      .andExpect(jsonPath("$.content").isArray)
      //there should be no objects
      .andExpect(jsonPath("$.content", empty()))
      //number of elements should be also 0
      .andExpect(jsonPath("$.numberOfElements", is(0)))
      //but total number of elements should be 3
      .andExpect(jsonPath("$.totalElements", is(3)))
  }

  @Test
  def `04 existing glossary`() {
    mockMvc
      //call glossaries list with incorrect parameters
      .perform(get("/glossaries/1").contentType(MediaType.APPLICATION_JSON))
      //expect result is valid
      .andExpect(status().isOk)
      //expect JSON output
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      //and it has contain ID as 1
      .andExpect(jsonPath("$.id", is(1)))

  }

  @Test
  def `05 non-existing glossary`() {
    mockMvc
      //call glossaries list with incorrect parameters
      .perform(get("/glossaries/100").contentType(MediaType.APPLICATION_JSON))
      //expect result is invalid
      .andExpect(status().isBadRequest)
      //and message should contain ID of wrong value
      .andExpect(content().string(containsString("100")))
  }

  @Test
  def `06 add valid glossary`() {
    val glossary = new Glossary()
    glossary.setName("Test glossary")
    glossary.setDescription("Test glossary's description")

    mockMvc
      //try to add a new glossary
      .perform(put("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary))
      )
      //expect result is valid
      .andExpect(status().isOk)
      //and content is empty
      .andExpect(content().string(""))
  }

  @Test
  def `07 add glossary with predefined ID`() {
    val glossary = new Glossary()
    glossary.setId(100500L)
    glossary.setName("Test glossary")
    glossary.setDescription("Test glossary's description")

    mockMvc
      //try to add a new glossary
      .perform(put("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary))
      )
      //expect result is invalid
      .andExpect(status().isBadRequest)
  }

  @Test
  def `08 add invalid glossary`() {
    val glossary = new Glossary()
    glossary.setName(null) //incorrect value
    glossary.setDescription("Test glossary's description")

    mockMvc
      //try to add a new glossary, which contains invalid value
      .perform(put("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary))
      )
      //expect result is invalid
      .andExpect(status().isBadRequest)
      //content type is JSON
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      //error should be about name
      .andExpect(jsonPath("$.name").value(not(nullValue())))
      //but not description
      .andExpect(jsonPath("$.description").doesNotExist())
  }

  @Test
  def `09 update valid glossary`() {
    val glossary = new Glossary()
    glossary.setId(2L)
    glossary.setName("Test valid glossary")
    glossary.setDescription("Test glossary's description")

    mockMvc
      //try to update existing glossary
      .perform(post("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary))
      )
      //expect result is valid
      .andExpect(status().isOk)
      //and content is empty
      .andExpect(content().string(""))
  }

  @Test
  def `10 update invalid glossary`() {
    val glossary = new Glossary()
    glossary.setId(2L)
    glossary.setName(null)
    glossary.setDescription("Doesn't matter") //incorrect value

    mockMvc
      //try to update existing glossary, which contains invalid value
      .perform(post("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary))
      )
      //expect result is invalid
      .andExpect(status().isBadRequest)
      //content type is JSON
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      //error should be about description
      .andExpect(jsonPath("$.name").value(not(nullValue())))
      //but not name
      .andExpect(jsonPath("$.description").doesNotExist())
  }

  @Test
  def `11 update glossary with non existing ID`() {
    val glossary = new Glossary()
    glossary.setId(100L)
    glossary.setName("Test not-existing glossary")
    glossary.setDescription("Test glossary description")

    mockMvc
      //try to update existing glossary, which contains non-existing ID
      .perform(post("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary))
      )
      //expect result is valid
      .andExpect(status().isOk)
      //and content is empty
      .andExpect(content().string(""))
  }

  @Test
  def `12 update glossary with null ID`() {
    val glossary = new Glossary()
    glossary.setId(null)
    glossary.setName("Test null-IDs glossary")
    glossary.setDescription("Test glossary description")

    mockMvc
      //try to update existing glossary, which contains null ID value
      .perform(post("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary))
      )
      //expect result is valid
      .andExpect(status().isOk)
      //and content is empty
      .andExpect(content().string(""))
  }

  @Test
  def `13 remove existing glossary`() {
    mockMvc
      //call glossaries with correct glossary ID
      .perform(post("/glossaries")
        .contentType(MediaType.ALL)
        .param("glossaryId", "1")
      )
      //everything should be OK
      .andExpect(status().isOk)
  }

  @Test
  def `14 remove non-existing glossary`() {
    mockMvc
      //call glossaries with incorrect glossary ID
      .perform(post("/glossaries")
        .contentType(MediaType.ALL)
        .param("glossaryId", "100")
      )
      //it should return error status
      .andExpect(status().isBadRequest)
      //and message should contain ID of wrong value
      .andExpect(content().string(containsString("100")))
  }

}