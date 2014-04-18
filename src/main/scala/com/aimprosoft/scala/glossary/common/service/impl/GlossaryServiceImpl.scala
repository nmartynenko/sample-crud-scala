package com.aimprosoft.scala.glossary.common.service.impl

import com.aimprosoft.scala.glossary.common.exception.{NoGlossaryFoundException, GlossaryException}
import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import com.aimprosoft.scala.glossary.common.persistence.GlossaryPersistence
import com.aimprosoft.scala.glossary.common.service.GlossaryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class GlossaryServiceImpl extends BaseCrudServiceImpl[Glossary, GlossaryPersistence] with GlossaryService {

  @Autowired
  protected val persistence: GlossaryPersistence = null

  @throws[GlossaryException]
  override def getById(glossaryId: java.lang.Long): Glossary = {
    try {
      val glossary = super.getById(glossaryId)

      if (glossary == null) {
        throw new NoGlossaryFoundException(null, glossaryId)
      }

      glossary
    } catch {
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  override def add(glossary: Glossary): Unit = {
    try {
      super.add(glossary)
    } catch {
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  override def update(glossary: Glossary): Unit = {
    try {
      super.update(glossary)
    } catch {
      case e: EmptyResultDataAccessException => throw new NoGlossaryFoundException(e, glossary.id)
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  override def remove(glossary: Glossary): Unit = {
    try {
      super.remove(glossary)
    } catch {
      case e: EmptyResultDataAccessException => throw new NoGlossaryFoundException(e, glossary.id)
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  override def removeById(glossaryId: java.lang.Long): Unit = {
    try {
      super.removeById(glossaryId)
    } catch {
      case e: EmptyResultDataAccessException => throw new NoGlossaryFoundException(e, glossaryId)
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }
}
