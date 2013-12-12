package com.aimprosoft.scala.contrib.spring.oval;

import java.lang.annotation.*;

//it's made as Java annotation for interoperability
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SpringValidateNestedProperty {
}
