package com.aimprosoft.scala.glossary.common.model.impl

import com.aimprosoft.scala.glossary.common.model.BusinessModel
import javax.persistence._
import net.sf.oval.constraint.{NotEmpty, NotNull}
import scala.beans.BeanProperty

@Entity
@Table(name = "glossary")
class Glossary extends BusinessModel {

  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @NotEmpty(message = "sample.error.not.empty")
  //hibernate
  @Column(name = "name", nullable = false, unique = true)
  var name: String = null

  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @NotEmpty(message = "sample.error.not.empty")
  //hibernate
  @Lob
  @Column(name = "description", nullable = true, length = 4096)
  var description: String = null

}
