package com.aimprosoft.scala.glossary.common.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.{NoOpPasswordEncoder, PasswordEncoder}
import org.springframework.stereotype.Service
import com.aimprosoft.scala.glossary.common.model.impl.User
import com.aimprosoft.scala.glossary.common.persistence.UserPersistence
import com.aimprosoft.scala.glossary.common.service.UserService
import com.aimprosoft.scala.glossary.common.model.UserRole

@Service
class UserServiceImpl extends BaseCrudServiceImpl[User, UserPersistence] with UserService {

  @Autowired
  protected val persistence: UserPersistence = null

  @Autowired(required = false)
  private val passwordEncoder: PasswordEncoder = NoOpPasswordEncoder.getInstance

  override def add(user: User): Unit = {
    user.password = passwordEncoder.encode(user.password)

    super.add(user)
  }

  def getByEmail(username: String): User = {
    persistence.findByEmail(username)
  }

  override def countByRole(role: UserRole): Long = {
    persistence.countByRole(role)
  }
}
