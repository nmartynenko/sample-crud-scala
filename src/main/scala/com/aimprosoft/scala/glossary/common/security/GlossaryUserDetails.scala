package com.aimprosoft.scala.glossary.common.security

import org.springframework.security.core.GrantedAuthority
import java.util.Collection
import com.aimprosoft.scala.glossary.common.model.impl.User
import scala.beans.BeanProperty

case class GlossaryUserDetails(var username: String,
                               var password: String,
                               authorities: Collection[GrantedAuthority]

                                )
  extends org.springframework.security.core.userdetails.User(username, password, authorities) {

  @BeanProperty
  var user: User = null
}
