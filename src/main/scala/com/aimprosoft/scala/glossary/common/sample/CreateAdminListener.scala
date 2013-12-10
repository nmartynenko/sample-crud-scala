package com.aimprosoft.scala.glossary.common.sample

import com.aimprosoft.scala.glossary.common.model.UserRole
import com.aimprosoft.scala.glossary.common.model.impl.User
import com.aimprosoft.scala.glossary.common.service.UserService
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
;

@Service
class CreateAdminListener {

  private val _logger = LoggerFactory.getLogger(getClass)

  @Autowired
  private val userService: UserService = null

  @PostConstruct
  @throws[Exception]
  def init() {
    _logger.info("Start adding sample admin")

    val user = new User()
    user.email = "admin@example.com"
    user.password = "admin"
    user.name = "Sample Admin"
    user.role = UserRole.ADMIN

    userService.addUser(user)

    _logger.info("Sample admin has been added successfully")
  }
}
