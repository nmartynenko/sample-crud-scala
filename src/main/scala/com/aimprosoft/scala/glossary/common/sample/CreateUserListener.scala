package com.aimprosoft.scala.glossary.common.sample

import com.aimprosoft.scala.glossary.common.model.UserRole
import com.aimprosoft.scala.glossary.common.model.impl.User
import com.aimprosoft.scala.glossary.common.service.UserService
import com.typesafe.scalalogging.slf4j.StrictLogging
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CreateUserListener extends StrictLogging {

  @Autowired
  private val userService: UserService = null

  @PostConstruct
  def init() {
    if (userService.countByRole(UserRole.USER) == 0) {

      logger.info("Start adding sample user")

      val user = User(
        email = "user@example.com",
        password = "user",
        name = "Sample User",
        role = UserRole.USER
      )

      userService.add(user)

      logger.info("Sample user has been added successfully")
    }
  }
}
