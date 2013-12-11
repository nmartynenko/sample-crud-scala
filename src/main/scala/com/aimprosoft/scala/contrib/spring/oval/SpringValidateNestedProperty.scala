package com.aimprosoft.scala.contrib.spring.oval

import java.lang.annotation._

//it's made as Java annotation for interoperability
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(Array(ElementType.FIELD))
abstract class SpringValidateNestedProperty extends Annotation