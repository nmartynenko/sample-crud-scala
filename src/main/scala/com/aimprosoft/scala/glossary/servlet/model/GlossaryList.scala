package com.aimprosoft.scala.glossary.servlet.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.data.domain.Page
import com.aimprosoft.scala.glossary.common.model.impl.Glossary
import scala.beans.BeanProperty
;

/**
 * This simple wrapper for ignoring some properties of org.springframework.data.domain.Page, f.e. "iterator"
 */
@JsonRootName("result")
@JsonIgnoreProperties(Array("iterator"))
class GlossaryList(p: Page[Glossary]){

  @BeanProperty
  @JsonUnwrapped
  var page = p
}