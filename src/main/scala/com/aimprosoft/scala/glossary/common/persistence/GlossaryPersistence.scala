package com.aimprosoft.scala.glossary.common.persistence

import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import org.springframework.data.jpa.repository.JpaRepository

trait GlossaryPersistence extends JpaRepository[Glossary, java.lang.Long]