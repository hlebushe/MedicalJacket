package org.isf.controller.web;

import org.isf.dao.*;
import org.isf.models.ExaminationsModel;
import org.isf.repository.UserGroupRepository;
import org.isf.repository.UserRepository;
import org.isf.service.FilesService;
import org.isf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/users")
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FilesService filesService;

    @Autowired
    UserGroupRepository userGroupRepository;

    @Autowired
    protected ServletContext mContext;

    @GetMapping(value = "/list")
    public ModelAndView getUsers(Model model) throws IOException, ParseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(auth.getName());

        List<User> users = new ArrayList<User>();
        users = userRepository.findAll();

        ModelAndView mv = new ModelAndView();
        mv.addObject("userName", "Welcome " + user.getUserName());
        mv.addObject("users", users);
        mv.setViewName("users_list");
        return mv;
    }

    @GetMapping(value = "/add")
    public ModelAndView getAddUser(Model model) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("user", new User());
        mv.setViewName("user_add");
        return mv;
    }

    @PostMapping("/add")
    public ModelAndView addUser(@RequestParam("photo") MultipartFile photo, @Valid User user, BindingResult result, Model model) throws IOException, SQLException {
        user.setPhoto(filesService.getBlobData(photo));

        user.setAge();
        user.setName();
        UserGroup userGroup = userGroupRepository.findByCode("admin");
        user.setUserGroupName(userGroup);
        user.setUserName(user.getEmail());

        User userNew = userService.saveUser(user);

        return new ModelAndView(new RedirectView(mContext.getContextPath() +"/users/list"));
    }
}
