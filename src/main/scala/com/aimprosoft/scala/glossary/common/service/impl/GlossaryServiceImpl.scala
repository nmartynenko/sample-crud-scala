package com.aimprosoft.scala.glossary.common.service.impl

import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import com.aimprosoft.scala.glossary.common.persistence.GlossaryPersistence
import com.aimprosoft.scala.glossary.common.service.GlossaryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class GlossaryServiceImpl extends BaseCrudServiceImpl[Glossary, GlossaryPersistence] with GlossaryService {

  @Autowired
  protected val persistence: GlossaryPersistence = null

  @PreAuthorize("hasRole('ADMIN')")
  override def add(glossary: Glossary): Unit = {
      super.add(glossary)
  }

  @PreAuthorize("hasRole('ADMIN')")
  override def update(glossary: Glossary): Unit = {
      super.update(glossary)
  }

  @PreAuthorize("hasRole('ADMIN')")
  override def remove(glossary: Glossary): Unit = {
    super.remove(glossary)
  }

  @PreAuthorize("hasRole('ADMIN')")
  override def removeById(glossaryId: java.lang.Long): Unit = {
    super.removeById(glossaryId)
  }
}
