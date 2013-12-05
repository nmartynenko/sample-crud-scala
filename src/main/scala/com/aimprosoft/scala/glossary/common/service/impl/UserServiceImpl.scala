package com.aimprosoft.scala.glossary.common.service.impl

import org.springframework.stereotype.Service
import com.aimprosoft.scala.glossary.common.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import scala.beans.BeanProperty
import com.aimprosoft.scala.glossary.common.persistence.UserPersistence
import org.springframework.security.crypto.password.PasswordEncoder
import com.aimprosoft.scala.glossary.common.model.impl.User

@Service
class UserServiceImpl extends UserService {

  @BeanProperty
  @Autowired
  var userPersistence: UserPersistence = null

  @BeanProperty
  @Autowired
  var passwordEncoder: PasswordEncoder = null

  def addUser(user: User): Unit = {
    user.setPassword(passwordEncoder.encode(user.getPassword))

    userPersistence.save(user)
  }

  def getUserByEmail(username: String): User = {
    userPersistence.findByEmail(username)
  }
}
