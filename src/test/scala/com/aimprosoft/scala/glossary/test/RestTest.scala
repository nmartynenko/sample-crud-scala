package com.aimprosoft.scala.glossary.test

import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import org.hamcrest.Matchers._
import org.junit.runners.MethodSorters
import org.junit.{FixMethodOrder, Test}
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._
import org.springframework.test.context.{ContextConfiguration, ContextHierarchy}
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.junit.runner.RunWith


@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextHierarchy(
  Array(
    new ContextConfiguration(
      name = "base",
      //include sample data
      locations = Array("classpath:spring/spring-sample-data.xml"),
      inheritLocations = true
    )
  )
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RestTest extends BaseTest {

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
    glossary.name = "Test glossary"
    glossary.description = "Test glossary's description"

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
  def `07 add glossary with non existing ID`() {
    val glossary = new Glossary()
    glossary.id = 100500L
    glossary.name = "Try to add not-existing glossary"
    glossary.description = "Test glossary description"

    mockMvc
      //try to update existing glossary, which contains non-existing ID
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
  def `08 add glossary with existing name`() {
    val glossary = new Glossary()
    glossary.id = 12345L
    glossary.name = "Test glossary"
    glossary.description = "Test glossary's description"

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
  def `09 add invalid glossary`() {
    val glossary = new Glossary()
    glossary.name = null //incorrect value
    glossary.description = "Test glossary's description"

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
  def `10 update valid glossary`() {
    val glossary = new Glossary()
    glossary.id = 2L
    glossary.name = "Test valid glossary"
    glossary.description = "Test glossary's description"

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
  def `11 update invalid glossary`() {
    val glossary = new Glossary()
    glossary.id = 2L
    glossary.name = null //incorrect value
    glossary.description = "Doesn't matter"

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
  def `12 update glossary with non existing ID`() {
    val glossary = new Glossary()
    glossary.id = 100L
    glossary.name = "Test not-existing glossary"
    glossary.description = "Test glossary description"

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
  def `13 update glossary with null ID`() {
    val glossary = new Glossary()
    glossary.id = null
    glossary.name = "Test null-IDs glossary"
    glossary.description = "Test glossary description"

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
  def `14 remove existing glossary`() {
    mockMvc
      //call glossaries with correct glossary ID
      .perform(delete("/glossaries/1").contentType(MediaType.ALL))
      //everything should be OK
      .andExpect(status().isOk)
  }

  @Test
  def `15 remove non-existing glossary`() {
    mockMvc
      //call glossaries with incorrect glossary ID
      .perform(delete("/glossaries/100").contentType(MediaType.ALL))
      //it should return error status
      .andExpect(status().isBadRequest)
      //and message should contain ID of wrong value
      .andExpect(content().string(containsString("100")))
  }

}