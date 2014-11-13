package com.aimprosoft.scala.glossary.util

import org.junit.runner.notification.{RunListener, RunNotifier}
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

class RunListenerSpringJUnit4ClassRunner(private val clazz: Class[_])
  extends SpringJUnit4ClassRunner(clazz) {

  private val runListener: RunListener = {
    clazz.getAnnotation(classOf[WithRunListener]) match {
      case annotation: WithRunListener =>
        annotation.value.newInstance()
      case _ =>
        null
    }
  }

  override def run(notifier: RunNotifier): Unit = {
    Option(runListener) foreach {listener =>
        notifier.addListener(listener)
    }

    super.run(notifier)
  }
}
