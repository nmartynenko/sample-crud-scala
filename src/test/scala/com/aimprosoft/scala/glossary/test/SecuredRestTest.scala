package com.aimprosoft.scala.glossary.test

import com.aimprosoft.scala.glossary.common.model.UserRole
import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import com.aimprosoft.scala.glossary.util._
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.junit.{After, Test, FixMethodOrder}
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.{ContextHierarchy, ContextConfiguration}
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._

@RunWith(classOf[RunListenerSpringJUnit4ClassRunner])
@WithRunListener(classOf[InvokeAsRunListener])
@ContextHierarchy(
  Array(
    new ContextConfiguration(
      name = "base",
      locations = Array("classpath:spring/spring-security.xml"),//include security
      inheritLocations = true
    ),
    new ContextConfiguration(
      name = "servlet",
      locations = Array("classpath:servlet/glossary-servlet-security.xml"),//include security
      inheritLocations = true
    )
  )
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SecuredRestTest extends BaseTest {

  @Test
  def `00 dummy test for making RunListener to work as expected`() = ()

  @After
  def clearContext() {
    //clear security context after tests execution
    SecurityContextHolder.clearContext()
  }

  @Test
  def `01 try to get glossaries anonymously`() {
    mockMvc
      //call glossaries list without parameters
      .perform(get("/glossaries").contentType(MediaType.APPLICATION_JSON))
      //expect result is OK, as there is no @PreAuthorize annotation
      .andExpect(status().isOk)
  }

  @Test
  @InvokeAs(UserRole.USER)
  def `02 try to add glossary as USER`() {
    val glossary = new Glossary
    glossary.name = "User's glossary"
    glossary.description = "I want to add this"

    mockMvc
      //try to add new glossary
      .perform(post("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary)))
      //expect result is forbidden
      .andExpect(status().isForbidden)
  }

  @Test
  @InvokeAs(UserRole.ADMIN)
  def `03 try to add glossary as ADMIN`() {
    val glossary = new Glossary
    glossary.name = "Admin's glossary"
    glossary.description = "I shall add this!"

    mockMvc
      //try to add new glossary
      .perform(post("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary)))
      //expect result is valid
      .andExpect(status().isOk)
  }

}
