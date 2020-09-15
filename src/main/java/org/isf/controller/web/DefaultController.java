package org.isf.controller.web;

import org.isf.dao.DeviceDetails;
import org.isf.dao.User;
import org.isf.repository.UserRepository;
import org.isf.service.DeviceDetailsService;
import org.isf.service.PatientService;
import org.isf.service.UserService;
import org.isf.service.XLSXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class DefaultController {

	private final UserRepository userRepository;

	@Autowired
	private PatientService patientService;

	@Autowired
	private DeviceDetailsService deviceDetailsService;

	@Autowired
	private UserService userService;

	@Autowired
	protected ServletContext mContext;

	@Autowired
	XLSXService xlsxService;

	@Autowired
	public DefaultController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping(value = "/login")
	public ModelAndView login() throws IOException {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@GetMapping(value = "/home")
	public ModelAndView home() throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUserName(auth.getName());

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
		List<User> users = userRepository.findAllByOrderByUserNameAsc();
		if (users.isEmpty()) {
			return "/403";
		} else {
			return "/home";
		}
	}

	@GetMapping(value = "/register")
	public ModelAndView register() throws IOException {
		ModelAndView mv = new ModelAndView();
		mv.addObject("device", new DeviceDetails());
		mv.setViewName("register");
		return mv;
	}

	@PostMapping("/register")
	public ModelAndView addUser(@Valid DeviceDetails deviceDetails, BindingResult result, Model model) throws IOException, SQLException {
		deviceDetails = deviceDetailsService.saveDevice(deviceDetails);
		userService.saveFromDevice(deviceDetails);

		return new ModelAndView(new RedirectView(mContext.getContextPath() +"/login"));
	}

}
