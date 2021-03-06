package com.aimprosoft.scala.glossary.common.model.impl

import javax.persistence._

import com.aimprosoft.scala.glossary.common.model.{BusinessModel, UserRole}
import net.sf.oval.constraint.{Email, NotEmpty, NotNull}

import scala.beans.BeanProperty

@Entity
@Table(name = "glossary_user")
class User extends BusinessModel {

  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @NotEmpty(message = "sample.error.not.empty")
  @Email(message = "sample.error.wrong.email")
  //hibernate
  @Column(name = "email", nullable = false, unique = true)
  var email: String = _

  @BeanProperty
  //validation
  @NotNull
  @NotEmpty
  //hibernate
  @Column(name = "password", nullable = false, unique = true)
  var password: String = _

  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @NotEmpty(message = "sample.error.not.empty")
  //hibernate
  @Column(name = "name", nullable = false)
  @BeanProperty
  var name: String = _

  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @Column(name = "user_role", nullable = false)
  @Enumerated(EnumType.STRING)
  var role: UserRole = _

  def canEqual(other: Any): Boolean = other.isInstanceOf[User]

  override def equals(other: Any): Boolean = other match {
    case that: User =>
      (that canEqual this) &&
        email == that.email &&
        password == that.password &&
        name == that.name &&
        role == that.role
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(email, password, name, role)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

//companion object for convenience and interoperability with Hibernate
object User {

  def apply(id: java.lang.Long = null,
             email: String = null,
             password: String = null,
             name: String = null,
             role: UserRole = null): User = {
    val user = new User

    user.id = id
    user.email = email
    user.password = password
    user.name = name
    user.role = role

    user
  }


  def unapply(user: User): Option[(Long, String, String, String, UserRole)] = {
    Some((user.id, user.email, user.password, user.name, user.role))
  }

}