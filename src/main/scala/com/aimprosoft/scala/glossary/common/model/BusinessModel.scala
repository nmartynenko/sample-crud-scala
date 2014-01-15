package com.aimprosoft.scala.glossary.common.model

import java.lang.Long
import javax.persistence._
import scala.beans.BeanProperty

@MappedSuperclass
abstract class BusinessModel extends Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @BeanProperty
    var id: Long = _

}
