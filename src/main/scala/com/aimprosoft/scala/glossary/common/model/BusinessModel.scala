package com.aimprosoft.scala.glossary.common.model

import javax.persistence._

import scala.beans.BeanProperty

@MappedSuperclass
abstract class BusinessModel extends Serializable
  with Equals { //enforce usage of overriding equals and hashCode

    @BeanProperty
    //hibernate
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    var id: java.lang.Long = _

}
