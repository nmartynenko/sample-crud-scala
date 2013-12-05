package com.aimprosoft.scala.glossary.common.sample

import com.aimprosoft.scala.glossary.common.model.UserRole
import com.aimprosoft.scala.glossary.common.model.impl.User
import com.aimprosoft.scala.glossary.common.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
;

@Service
class CreateAdminListener extends InitializingBean {

  private val _logger = LoggerFactory.getLogger(getClass)

  @Autowired
  private val userService: UserService = null

  @throws[Exception]
  def afterPropertiesSet() {
    _logger.info("Start adding sample admin")

    val user = new User()
    user.setEmail("admin@example.com")
    user.setPassword("admin")
    user.setName("Sample Admin")
    user.setRole(UserRole.ADMIN.toString)

    userService.addUser(user)

    _logger.info("Sample admin has been added successfully")
  }
}
