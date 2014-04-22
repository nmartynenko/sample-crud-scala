package com.aimprosoft.scala.glossary.common.service

import com.aimprosoft.scala.glossary.common.model.impl.{User, Glossary}
import com.aimprosoft.scala.glossary.common.model.{UserRole, BusinessModel}
import org.springframework.data.domain.Page
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
trait BaseCrudService[T <: BusinessModel] {

  def getCurrentPage(startRow: Int, pageSize: Int): Page[T]

  def exists(id: java.lang.Long): Boolean

  def count: Long

  def getById(id: java.lang.Long): T

  @Transactional(readOnly = false)
  def add(entity: T): Unit

  @Transactional(readOnly = false)
  def update(entity: T): Unit

  @Transactional(readOnly = false)
  def remove(entity: T): Unit

  @Transactional(readOnly = false)
  def removeById(id: java.lang.Long): Unit

}

trait GlossaryService extends BaseCrudService[Glossary]

trait UserService extends BaseCrudService[User]{

  def getByEmail(username: String): User

  def countByRole(role: UserRole): Long

}