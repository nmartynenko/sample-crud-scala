package com.aimprosoft.scala.glossary.common.persistence

import java.lang.Long
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import com.aimprosoft.scala.glossary.common.model.impl.User

@Transactional
trait UserPersistence extends JpaRepository[User, Long]{

    def findByEmail(email: String): User

}
