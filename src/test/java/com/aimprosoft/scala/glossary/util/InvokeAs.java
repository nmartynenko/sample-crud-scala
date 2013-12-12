package com.aimprosoft.scala.glossary.util;

import com.aimprosoft.scala.glossary.common.model.UserRole;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InvokeAs {
    UserRole value();
}
