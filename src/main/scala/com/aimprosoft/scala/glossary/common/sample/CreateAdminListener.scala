package com.aimprosoft.scala.glossary.common.sample

import com.aimprosoft.scala.glossary.common.model.UserRole
import com.aimprosoft.scala.glossary.common.model.impl.User
import com.aimprosoft.scala.glossary.common.service.UserService
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.typesafe.scalalogging.slf4j.StrictLogging
;

@Service
class CreateAdminListener extends StrictLogging {

  @Autowired
  private val userService: UserService = null

  @PostConstruct
  def init() {
    if (userService.countByRole(UserRole.ADMIN) == 0) {
      logger.info("Start adding sample admin")

      val user = new User()
      user.email = "admin@example.com"
      user.password = "admin"
      user.name = "Sample Admin"
      user.role = UserRole.ADMIN

      userService.add(user)

      logger.info("Sample admin has been added successfully")
    }
  }
}
