package com.aimprosoft.scala.glossary.common.sample

import javax.annotation.PostConstruct

import com.aimprosoft.scala.glossary.common.model.UserRole
import com.aimprosoft.scala.glossary.common.model.impl.User
import com.aimprosoft.scala.glossary.common.service.UserService
import com.typesafe.scalalogging.slf4j.StrictLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CreateAdminListener extends StrictLogging {

  @Autowired
  private val userService: UserService = null

  @PostConstruct
  def init() {
    if (userService.countByRole(UserRole.ADMIN) == 0) {
      logger.info("Start adding sample admin")

      val user = User(
        email = "admin@example.com",
        password = "admin",
        name = "Sample Admin",
        role = UserRole.ADMIN
      )

      userService.add(user)

      logger.info("Sample admin has been added successfully")
    }
  }
}
