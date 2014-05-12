package com.aimprosoft.scala.glossary.util

import org.junit.runner.notification.RunListener
import org.junit.runner.Description
import org.slf4j.LoggerFactory
import com.aimprosoft.scala.glossary.common.model.UserRole
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.{UsernamePasswordAuthenticationToken, AnonymousAuthenticationToken}
import org.springframework.security.core.authority.AuthorityUtils
import com.typesafe.scalalogging.slf4j.StrictLogging

class InvokeAsRunListener extends RunListener
  with InvokeAsRunListenerPredefinedRoles with StrictLogging {

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
    logger.debug("Start working on {} method", description.getMethodName)

    description.getAnnotation(classOf[InvokeAs]) match {
      case invokeAs: InvokeAs =>
        authenticateUser(invokeAs.value())
      case _ =>
        authenticateAnonymously()
    }

    logger.debug("End working on {} method", description.getMethodName)
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
