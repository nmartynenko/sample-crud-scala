package com.aimprosoft.scala.glossary.util;

import org.junit.runner.notification.RunListener;

import java.lang.annotation.*;

//this is also is easier to handle with Java annotations
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WithRunListener {
    Class<? extends RunListener> value();
}
