package com.aimprosoft.scala.glossary.common.model.impl

import com.aimprosoft.scala.glossary.common.model.BusinessModel
import javax.persistence._
import net.sf.oval.constraint.{Email, NotEmpty, NotNull}
import scala.beans.BeanProperty


@Entity
@Table(name = "glossary_user")
class User extends BusinessModel {

  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @NotEmpty(message = "sample.error.not.empty")
  @Email(message = "sample.error.wrong.email")
  //hibernate
  @Column(name = "email", nullable = false, unique = true)
  var email: String = null

  @BeanProperty
  //validation
  @NotNull
  @NotEmpty
  //hibernate
  @Column(name = "password", nullable = false, unique = true)
  var password: String = null

  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @NotEmpty(message = "sample.error.not.empty")
  //hibernate
  @Column(name = "name", nullable = false)
  @BeanProperty
  var name: String = null


  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @Column(name = "user_role", nullable = false)
  //todo what about enum?
  var role: String = null

}