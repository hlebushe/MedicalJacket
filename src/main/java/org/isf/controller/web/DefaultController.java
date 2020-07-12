package org.isf.controller.web;

import org.isf.dao.User;
import org.isf.dao.Patient;
import org.isf.repository.UserRepository;
import org.isf.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

	private final UserRepository userService;

	@Autowired
	private PatientService patientService;

	@Autowired
	public DefaultController(UserRepository userService) {
		this.userService = userService;
	}

	@GetMapping(value = "/login")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@GetMapping(value = "/home")
	public ModelAndView home() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByUserName(auth.getName());

		ModelAndView mv = new ModelAndView();
		mv.addObject("userName", "Welcome " + user.getUserName() +"!");
		mv.setViewName("home");
		return mv;
	}

	@GetMapping("/403")
	public String error403() {
		return "/403";
	}

	@GetMapping("/test")
	public String test() {
		List<User> users = userService.findAllByOrderByUserNameAsc();
		if (users.isEmpty()) {
			return "/403";
		} else {
			return "/home";
		}
	}
}
