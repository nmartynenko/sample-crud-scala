package com.aimprosoft.scala.glossary.servlet.model

import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonRootName, JsonUnwrapped}
import org.springframework.data.domain.Page

/**
 * This simple wrapper for ignoring some properties of org.springframework.data.domain.Page, f.e. "iterator"
 */
@JsonRootName("result")
@JsonIgnoreProperties(Array("iterator"))
case class GlossaryList(@JsonUnwrapped page: Page[Glossary])