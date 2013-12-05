package com.aimprosoft.scala.glossary.common.persistence

import com.aimprosoft.scala.glossary.common.model.impl.User
import java.lang.Long
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
trait UserPersistence extends JpaRepository[User, Long]{

    def findByEmail(email: String): User

}
