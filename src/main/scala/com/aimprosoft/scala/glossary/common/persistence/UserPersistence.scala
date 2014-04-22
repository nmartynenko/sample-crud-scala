package com.aimprosoft.scala.glossary.common.persistence

import com.aimprosoft.scala.glossary.common.model.UserRole
import com.aimprosoft.scala.glossary.common.model.impl.User
import org.springframework.data.jpa.repository.{Query, JpaRepository}
import org.springframework.data.repository.query.Param

trait UserPersistence extends JpaRepository[User, java.lang.Long] {

  def findByEmail(email: String): User

  @Query("select count(u) from User u where u.role = :role")
  def countByRole(@Param("role") role: UserRole): Long

}
