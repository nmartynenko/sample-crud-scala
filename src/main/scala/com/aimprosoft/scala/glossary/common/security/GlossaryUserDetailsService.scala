package com.aimprosoft.scala.glossary.common.security

import com.aimprosoft.scala.glossary.common.model.UserRole
import com.aimprosoft.scala.glossary.common.model.impl.User
import com.aimprosoft.scala.glossary.common.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService, UsernameNotFoundException}

class GlossaryUserDetailsService extends UserDetailsService {

  @Autowired
  private val userService: UserService = null

  @throws[UsernameNotFoundException]
  def loadUserByUsername(username: String): UserDetails = {
    val maybeUser = Option(userService.getByEmail(username))

    (maybeUser map { user =>
      GlossaryUserDetails(
        username, user.password, getGrantedAuthorities(user),
        //set actual DB user for possible further purposes
        user
      )
    }).orNull
  }

  private def getGrantedAuthorities(user: User) = {
    user.role match {
      case UserRole.ADMIN =>
        GlossaryUserDetailsService.ADMIN_AUTHORITIES
      case UserRole.USER =>
        GlossaryUserDetailsService.USER_AUTHORITIES
      case _ =>
        Nil
    }
  }
}

//set of constants
object GlossaryUserDetailsService{
  private def rolesToAuthorities(roles: List[UserRole]): List[SimpleGrantedAuthority] = {
    roles map { role =>
      new SimpleGrantedAuthority(role.toString)
    }
  }

  val ADMIN_ROLES = List(UserRole.USER, UserRole.ADMIN)
  val ADMIN_AUTHORITIES = rolesToAuthorities(ADMIN_ROLES)

  val USER_ROLES = List(UserRole.USER)
  val USER_AUTHORITIES = rolesToAuthorities(USER_ROLES)
}
