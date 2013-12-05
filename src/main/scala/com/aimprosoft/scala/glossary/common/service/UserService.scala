package com.aimprosoft.scala.glossary.common.service

import com.aimprosoft.scala.glossary.common.model.impl.User

trait UserService {

    def addUser(user: User)

    def getUserByEmail(username: String): User
}