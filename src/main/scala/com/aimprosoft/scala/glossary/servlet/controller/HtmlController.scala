package com.aimprosoft.scala.glossary.servlet.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Controller which simply handles *.html requests
 */
@Controller
class HtmlController extends BaseController {

  @RequestMapping(value = Array("/", "index.html"))
  def index: String = {
    "/index"
  }

  @RequestMapping(value = Array("login.html"))
  def login: String = {
    "/login"
  }

}