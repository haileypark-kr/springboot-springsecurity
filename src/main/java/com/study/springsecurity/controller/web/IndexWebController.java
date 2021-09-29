package com.study.springsecurity.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexWebController {

	@GetMapping("/index")
	public String indexMapping() {
		return "index";
	}
}
