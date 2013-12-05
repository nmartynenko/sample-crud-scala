package com.aimprosoft.scala.glossary.servlet.controller

import com.aimprosoft.scala.glossary.common.service.GlossaryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import scala.beans.BeanProperty
import org.springframework.web.bind.annotation._
import com.aimprosoft.scala.glossary.common.persistence.UserPersistence
import org.springframework.http.MediaType
import com.aimprosoft.scala.glossary.common.exception.GlossaryException
import com.aimprosoft.scala.glossary.servlet.model.GlossaryList
import scala.throws
import org.springframework.validation.annotation.Validated
import com.aimprosoft.scala.glossary.common.model.impl.Glossary

/**
 * Glossaries REST-controller. It produces and consumes JSON. For "USER" role all actions are read-only.
 *
 * @see com.aimprosoft.glossary.common.service.GlossaryService
 */
@Controller
class GlossaryRestController extends BaseController {

  @BeanProperty
  @Autowired
  var glossaryService: GlossaryService = null

  @BeanProperty
  @Autowired
  var userPersistence: UserPersistence = null

  @RequestMapping(value = Array("/glossaries"),
    method = Array(RequestMethod.GET),
    produces = Array(
      MediaType.APPLICATION_JSON_VALUE
    ))
  @ResponseBody
  @throws[GlossaryException]
  def getGlossaries(@RequestParam(value = "startRow", required = false, defaultValue = "0") startRow: Int,
                    @RequestParam(value = "pageSize", required = false, defaultValue = "0") pageSize: Int) = {
    new GlossaryList(glossaryService.getCurrentPage(startRow, pageSize))
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

  @RequestMapping(value = Array("/glossaries"),
    //method = Array(RequestMethod.DELETE))
    //only GET, POST, PUT allowed
    method = Array(RequestMethod.POST)
  )
  @ResponseBody
  @throws[GlossaryException]
  def removeGlossary(@RequestParam("glossaryId") glossaryId: Long) {
    glossaryService.removeGlossaryById(glossaryId)
  }
}