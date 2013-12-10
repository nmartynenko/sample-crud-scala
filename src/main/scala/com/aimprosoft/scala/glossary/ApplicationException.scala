package com.aimprosoft.scala.glossary

abstract class ApplicationException(message: String, cause: Throwable)
  extends Exception(message, cause)