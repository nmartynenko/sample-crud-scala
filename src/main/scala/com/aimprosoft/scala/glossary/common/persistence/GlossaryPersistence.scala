package com.aimprosoft.scala.glossary.common.persistence

import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
trait GlossaryPersistence extends JpaRepository[Glossary, java.lang.Long]