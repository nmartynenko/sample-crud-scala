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
  var name: String = _

  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @NotEmpty(message = "sample.error.not.empty")
  //hibernate
  @Lob
  @Column(name = "description", nullable = true, length = 4096)
  var description: String = _

  def canEqual(other: Any): Boolean = other.isInstanceOf[Glossary]

  override def equals(other: Any): Boolean = other match {
    case that: Glossary =>
      (that canEqual this) &&
        name == that.name &&
        description == that.description
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, description)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

//companion object for convenience and interoperability with Hibernate
object Glossary {

  def apply(id: java.lang.Long = null,
            name: String = null,
            description: String = null): Glossary = {
    val glossary: Glossary = new Glossary

    glossary.id = id
    glossary.name = name
    glossary.description = description

    glossary
  }

}
