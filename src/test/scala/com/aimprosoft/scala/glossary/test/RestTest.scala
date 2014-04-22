package com.aimprosoft.scala.glossary.test

import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import org.hamcrest.Matchers._
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.{ContextConfiguration, ContextHierarchy}
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._


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
class RestTest extends BaseTest {

  @Test
  def `glossaries list should return all items`() {
    val glossariesCount = glossaryPersistence.count().toInt

    mockMvc
      //call glossaries list without parameters
      .perform(get("/glossaries").contentType(MediaType.APPLICATION_JSON))
      //expect result is valid
      .andExpect(status().isOk)
      //expect JSON output
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      //there should be content
      .andExpect(jsonPath("$.content").isArray)
      //there should be all objects
      .andExpect(jsonPath("$.content").value(hasSize(glossariesCount)))
      //number of elements should be the same value
      .andExpect(jsonPath("$.numberOfElements", is(glossariesCount)))
      //and total size should be also the same
      .andExpect(jsonPath("$.totalElements", is(glossariesCount)))
  }

  @Test
  def `glossaries list with pagination`() {
    val glossariesCount = glossaryPersistence.count().toInt

    val pageSize = 2

    //since start row equals to 0, then
    //expected size is equal to page size
    //if all number of elements is greater than page size
    val expectedSize = if (pageSize < glossariesCount) pageSize else glossariesCount

    mockMvc
      //call glossaries list with parameters
      .perform(get("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .param("startRow", "0")
        .param("pageSize", pageSize.toString)
      )
      //expect result is valid
      .andExpect(status().isOk)
      //expect JSON output
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      //there should be content
      .andExpect(jsonPath("$.content").isArray)
      //there should be as expected size
      .andExpect(jsonPath("$.content", hasSize(expectedSize)))
      //number of elements should be as expected size
      .andExpect(jsonPath("$.numberOfElements", is(expectedSize)))
      //total number of elements should be all number of elements
      .andExpect(jsonPath("$.totalElements", is(glossariesCount)))
  }

  @Test
  def `glossaries list with wrong pagination`() {
    val glossariesCount = glossaryPersistence.count().toInt

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
      //but total number of elements should be all number of elements
      .andExpect(jsonPath("$.totalElements", is(glossariesCount)))
  }

  @Test
  def `existing glossary`() {
    val id = 2

    //this glossary is present in DB
    assume(glossaryPersistence.exists(id))

    mockMvc
      //call glossaries list with incorrect parameters
      .perform(get(s"/glossaries/$id").contentType(MediaType.APPLICATION_JSON))
      //expect result is valid
      .andExpect(status().isOk)
      //expect JSON output
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      //and it has contain ID as 1
      .andExpect(jsonPath("$.id", is(id)))
  }

  @Test
  def `non-existing glossary`() {
    val id = 100

    //this glossary is NOT present in DB
    assume(!glossaryPersistence.exists(id))

    mockMvc
      //call glossaries list with incorrect parameters
      .perform(get(s"/glossaries/$id").contentType(MediaType.APPLICATION_JSON))
      //expect result is invalid
      .andExpect(status().isBadRequest)
      //and message should contain ID of wrong value
      .andExpect(content().string(containsString(id.toString)))
  }

  @Test
  def `add valid glossary`() {
    val startGlossariesCount = glossaryPersistence.count()

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

    //data integrity check
    val endGlossariesCount = glossaryPersistence.count()

    //number of glossaries has increased by one
    assertResult(startGlossariesCount + 1)(endGlossariesCount)
  }

  @Test
  def `add glossary with non existing ID`() {
    val startGlossariesCount = glossaryPersistence.count()

    val glossary = new Glossary()
    glossary.id = 100500L
    glossary.name = "Try to add not-existing glossary"
    glossary.description = "Test glossary description"

    //this glossary is NOT present in DB
    assume(!glossaryPersistence.exists(glossary.id))

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

    //data integrity check
    val endGlossariesCount = glossaryPersistence.count()

    //number of glossaries has increased by one
    assertResult(startGlossariesCount + 1)(endGlossariesCount)

    //glossary with defined ID is NOT present in DB
    //as IDs are generated by DB
    assume(!glossaryPersistence.exists(glossary.id))
  }

  @Test
  def `add glossary with existing name`() {
    val startGlossariesCount = glossaryPersistence.count()

    val glossary = new Glossary()
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

    //data integrity check
    val endGlossariesCount = glossaryPersistence.count()

    //number of glossaries remains the same
    assertResult(startGlossariesCount)(endGlossariesCount)
  }

  @Test
  def `add invalid glossary`() {
    val startGlossariesCount = glossaryPersistence.count()

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

    //data integrity check
    val endGlossariesCount = glossaryPersistence.count()

    //number of glossaries remains the same
    assertResult(startGlossariesCount)(endGlossariesCount)
  }

  @Test
  def `update valid glossary`() {
    val id = 2L

    val glossary = new Glossary()
    glossary.id = id
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

    //data integrity check
    val dbGlossary = glossaryPersistence.findOne(id)

    //glossary taken from DB is the same as created one
    assertResult(dbGlossary)(glossary)
  }

  @Test
  def `update invalid glossary`() {
    val id = 2L

    val initialGlossary = glossaryPersistence.findOne(id)

    val glossary = new Glossary()
    glossary.id = id
    glossary.name = null //incorrect value
    glossary.description = "Doesn't matter"

    //initial glossary are not the same as created one
    assume(!initialGlossary.equals(glossary))

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

    //data integrity check
    val dbGlossary = glossaryPersistence.findOne(id)

    //glossary taken from DB is the same as initial one
    assertResult(dbGlossary)(initialGlossary)
  }

  @Test
  def `update glossary with non existing ID`() {
    val startGlossariesCount = glossaryPersistence.count()

    val glossary = new Glossary()
    glossary.id = 500100L
    glossary.name = "Test not-existing glossary"
    glossary.description = "Test glossary description"

    //this glossary is NOT present in DB
    assume(!glossaryPersistence.exists(glossary.id))

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

    //data integrity check
    val endGlossariesCount = glossaryPersistence.count()

    //number of glossaries has increased by one
    assertResult(startGlossariesCount + 1)(endGlossariesCount)

    //glossary with defined ID is NOT present in DB
    //as IDs are generated by DB
    assume(!glossaryPersistence.exists(glossary.id))
  }

  @Test
  def `update glossary with null ID`() {
    val startGlossariesCount = glossaryPersistence.count()

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

    //data integrity check
    val endGlossariesCount = glossaryPersistence.count()

    //number of glossaries has increased by one
    assertResult(startGlossariesCount + 1)(endGlossariesCount)
  }

  @Test
  def `remove existing glossary`() {
    val id = 1L

    //this glossary should be present in DB
    assume(glossaryPersistence.exists(id))

    val startGlossariesCount = glossaryPersistence.count()

    mockMvc
      //call glossaries with correct glossary ID
      .perform(delete(s"/glossaries/$id").contentType(MediaType.ALL))
      //everything should be OK
      .andExpect(status().isOk)

    //data integrity check
    val endGlossariesCount = glossaryPersistence.count()

    //number of glossaries has decreased by one
    assertResult(startGlossariesCount - 1)(endGlossariesCount)

    //this glossary is no longer present in DB
    assume(!glossaryPersistence.exists(id))
  }

  @Test
  def `remove non-existing glossary`() {
    val id = 99L

    //this id should NOT be present in DB
    assume(!glossaryPersistence.exists(id))

    val startGlossariesCount = glossaryPersistence.count()

    mockMvc
      //call glossaries with incorrect glossary ID
      .perform(delete(s"/glossaries/$id").contentType(MediaType.ALL))
      //it should return error status
      .andExpect(status().isBadRequest)
      //and message should contain ID of wrong value
      .andExpect(content().string(containsString(id.toString)))

    //data integrity check
    val endGlossariesCount = glossaryPersistence.count()

    //number of glossaries remains the same
    assertResult(startGlossariesCount)(endGlossariesCount)
  }

}