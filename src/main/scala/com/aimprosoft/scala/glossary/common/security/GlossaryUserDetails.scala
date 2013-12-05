package com.aimprosoft.scala.glossary.common.security

import com.aimprosoft.scala.glossary.common.model.impl.User
import java.util
import org.springframework.security.core.GrantedAuthority
import scala.beans.BeanProperty

case class GlossaryUserDetails(var username: String,
                               var password: String,
                               authorities: util.Collection[GrantedAuthority])
  extends org.springframework.security.core.userdetails.User(username, password, authorities) {

  @BeanProperty
  var user: User = null
}
