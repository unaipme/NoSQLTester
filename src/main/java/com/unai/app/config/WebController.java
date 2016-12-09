package com.unai.app.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/")
@ApiIgnore
public class WebController {
	
	@GetMapping
	public RedirectView redirect() {
		return new RedirectView("/swagger-ui.html");
	}
	
}
