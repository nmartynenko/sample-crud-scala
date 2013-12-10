package com.aimprosoft.scala.glossary.servlet.controller

import com.aimprosoft.scala.glossary.common.exception.GlossaryException
import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import com.aimprosoft.scala.glossary.common.service.GlossaryService
import com.aimprosoft.scala.glossary.servlet.model.GlossaryList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation._
import scala.throws

/**
 * Glossaries REST-controller. It produces and consumes JSON. For "USER" role all actions are read-only.
 *
 * @see com.aimprosoft.glossary.common.service.GlossaryService
 */
@Controller
class GlossaryRestController extends BaseController {

  @Autowired
  private val glossaryService: GlossaryService = null

  @RequestMapping(value = Array("/glossaries"),
    method = Array(RequestMethod.GET),
    produces = Array(
      MediaType.APPLICATION_JSON_VALUE
    ))
  @ResponseBody
  @throws[GlossaryException]
  def getGlossaries(@RequestParam(value = "startRow", required = false, defaultValue = "0") startRow: Int,
                    @RequestParam(value = "pageSize", required = false, defaultValue = "0") pageSize: Int) = {
    GlossaryList(glossaryService.getCurrentPage(startRow, pageSize))
  }

  @RequestMapping(value = Array("/glossaries/{id}"),
    method = Array(RequestMethod.GET),
    produces = Array(
      MediaType.APPLICATION_JSON_VALUE
    ))
  @ResponseBody
  @throws[GlossaryException]
  def getGlossary(@PathVariable id: Long) = {
    glossaryService.getGlossaryById(id)
  }

  @RequestMapping(value = Array("/glossaries"),
    method = Array(RequestMethod.PUT),
    consumes = Array(
      MediaType.APPLICATION_JSON_VALUE
    ))
  @ResponseBody
  @throws[GlossaryException]
  def saveGlossary(@RequestBody @Validated glossary: Glossary) {
    glossaryService.addGlossary(glossary)
  }

  @RequestMapping(value = Array("/glossaries"),
    method = Array(RequestMethod.POST),
    consumes = Array(
      MediaType.APPLICATION_JSON_VALUE
    ))
  @ResponseBody
  @throws[GlossaryException]
  def updateGlossary(@RequestBody @Validated glossary: Glossary) {
    glossaryService.updateGlossary(glossary)
  }

  @RequestMapping(value = Array("/glossaries/{glossaryId}"),
    method = Array(RequestMethod.DELETE)
  )
  @ResponseBody
  @throws[GlossaryException]
  def removeGlossary(@PathVariable("glossaryId") glossaryId: Long) {
    glossaryService.removeGlossaryById(glossaryId)
  }
}