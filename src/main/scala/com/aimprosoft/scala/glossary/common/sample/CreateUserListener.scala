package com.aimprosoft.scala.glossary.common.sample

import org.springframework.stereotype.Service
import org.springframework.beans.factory.InitializingBean
import org.slf4j.LoggerFactory
import scala.beans.BeanProperty
import org.springframework.beans.factory.annotation.Autowired
import com.aimprosoft.scala.glossary.common.service.UserService
import com.aimprosoft.scala.glossary.common.model.impl.User
import com.aimprosoft.scala.glossary.common.model.UserRole

@Service
class CreateUserListener extends InitializingBean {

  private val _logger = LoggerFactory.getLogger(getClass)

  @BeanProperty
  @Autowired
  var userService: UserService = null

  @throws[Exception]
  def afterPropertiesSet() {
    _logger.info("Start adding sample user")

    val user = new User()
    user.setEmail("user@example.com")
    user.setPassword("user")
    user.setName("Sample User")
    user.setRole(UserRole.USER.toString)

    userService.addUser(user)

    _logger.info("Sample user has been added successfully")
  }
}
