package com.aimprosoft.scala.glossary.util

import org.junit.runner.notification.RunListener
import org.junit.runner.Description
import org.slf4j.LoggerFactory
import com.aimprosoft.scala.glossary.common.model.UserRole
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.{UsernamePasswordAuthenticationToken, AnonymousAuthenticationToken}
import org.springframework.security.core.authority.AuthorityUtils

class InvokeAsRunListener extends RunListener with InvokeAsRunListenerPredefinedRoles {

  private val _logger = LoggerFactory.getLogger(getClass)

  private def authenticateAnonymously(){
    authenticate(ANONYMOUS)
  }

  private def authenticateUser(role: UserRole) {
    role match {
      case UserRole.USER =>
        authenticate(USER)
      case UserRole.ADMIN =>
        authenticate(ADMIN)
    }
  }

  private def authenticate(authentication: Authentication){
    SecurityContextHolder.getContext.setAuthentication(authentication)
  }

  override def testStarted(description: Description): Unit = {
    _logger.debug("Start working on {} method", description.getMethodName)

    description.getAnnotation(classOf[InvokeAs]) match {
      case invokeAs: InvokeAs =>
        authenticateUser(invokeAs.value())
      case _ =>
        authenticateAnonymously()
    }

    _logger.debug("End working on {} method", description.getMethodName)
  }
}

sealed trait InvokeAsRunListenerPredefinedRoles {

  private def createUser(userRole: UserRole) = {
    new UsernamePasswordAuthenticationToken(
      userRole.name().toLowerCase,
      null,
      AuthorityUtils.createAuthorityList(userRole.name()))
  }

  val ANONYMOUS: Authentication =
    new AnonymousAuthenticationToken("key", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"))

  val USER: Authentication = createUser(UserRole.USER)

  val ADMIN: Authentication = createUser(UserRole.ADMIN)

}
