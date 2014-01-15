package com.aimprosoft.scala.glossary.common.service

import com.aimprosoft.scala.glossary.common.exception.GlossaryException
import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import scala.throws

trait GlossaryService {

  @throws[GlossaryException]
  def getCurrentPage(startRow: Int, pageSize: Int): Page[Glossary]

  @throws[GlossaryException]
  def getGlossaryById(glossaryId: java.lang.Long): Glossary

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def addGlossary(glossary: Glossary): Unit

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def updateGlossary(glossary: Glossary): Unit

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def removeGlossary(glossary: Glossary): Unit

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def removeGlossaryById(glossaryId: Long): Unit

}