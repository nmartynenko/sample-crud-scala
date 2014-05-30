package com.aimprosoft.scala.glossary.test

import com.aimprosoft.scala.glossary.common.model.UserRole
import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import com.aimprosoft.scala.glossary.util._
import org.junit.runner.RunWith
import org.junit.{After, Test}
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
      //include security
      locations = Array("classpath:spring/spring-security.xml"),
      inheritLocations = true
    ),
    new ContextConfiguration(
      name = "servlet",
      //include security
      locations = Array("classpath:servlet/glossary-servlet-security.xml"),
      inheritLocations = true
    )
  )
)
class SecuredRestTest extends BaseTest {

  @After
  def clearContext() {
    //clear security context after tests execution
    SecurityContextHolder.clearContext()
  }

  @Test
  def `try to get glossaries anonymously`() {
    mockMvc
      //call glossaries list without parameters
      .perform(get("/glossaries").contentType(MediaType.APPLICATION_JSON))
      //expect result is OK, as there is no @PreAuthorize annotation
      //it's secured with security:intercept-url mechanism
      .andExpect(status().isOk)
  }

  @Test
  @InvokeAs(UserRole.USER)
  def `try to add glossary as USER`() {
    val startGlossariesCount = glossaryPersistence.count()

    val glossary = Glossary(
      name = "User's glossary",
      description = "I want to add this"
    )

    mockMvc
      //try to add new glossary
      .perform(post("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary)))
      //expect result is forbidden
      .andExpect(status().isForbidden)

    val endGlossariesCount = glossaryPersistence.count()

    //data integrity check
    //number of glossaries remains the same
    assertResult(startGlossariesCount)(endGlossariesCount)
  }

  @Test
  @InvokeAs(UserRole.ADMIN)
  def `try to add glossary as ADMIN`() {
    val startGlossariesCount = glossaryPersistence.count()

    val glossary = Glossary(
      name = "Admin's glossary",
      description = "I shall add this!"
    )

    mockMvc
      //try to add new glossary
      .perform(post("/glossaries")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(glossary)))
      //expect result is valid
      .andExpect(status().isOk)

    val endGlossariesCount = glossaryPersistence.count()

    //data integrity check
    //number of glossaries has increased by one
    assertResult(startGlossariesCount + 1)(endGlossariesCount)
  }

}
