package com.aimprosoft.scala.glossary.common.security

import com.aimprosoft.scala.glossary.common.model.impl.User
import org.springframework.security.core.GrantedAuthority
import scala.beans.BeanProperty

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

case class GlossaryUserDetails(username: String,
                               password: String,
                               authorities: List[_ <: GrantedAuthority],
                               user: User)
  extends org.springframework.security.core.userdetails.User(username, password, authorities)
