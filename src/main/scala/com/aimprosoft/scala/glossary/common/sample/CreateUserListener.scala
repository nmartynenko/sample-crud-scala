package com.aimprosoft.scala.glossary.common.sample

import com.aimprosoft.scala.glossary.common.model.UserRole
import com.aimprosoft.scala.glossary.common.model.impl.User
import com.aimprosoft.scala.glossary.common.service.UserService
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CreateUserListener {

  private val _logger = LoggerFactory.getLogger(getClass)

  @Autowired
  private val userService: UserService = null

  @PostConstruct
  @throws[Exception]
  def init() {
    _logger.info("Start adding sample user")

    val user = new User()
    user.email = "user@example.com"
    user.password = "user"
    user.name = "Sample User"
    user.role = UserRole.USER.toString

    userService.addUser(user)

    _logger.info("Sample user has been added successfully")
  }
}
