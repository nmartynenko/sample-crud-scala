package com.aimprosoft.scala.glossary.common.persistence

import com.aimprosoft.scala.glossary.common.model.impl.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
trait UserPersistence extends JpaRepository[User, java.lang.Long]{

    def findByEmail(email: String): User

}
