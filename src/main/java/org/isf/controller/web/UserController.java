package org.isf.controller.web;

import org.isf.dao.*;
import org.isf.models.ExaminationsModel;
import org.isf.repository.UserGroupRepository;
import org.isf.repository.UserRepository;
import org.isf.service.DeviceDetailsService;
import org.isf.service.FilesService;
import org.isf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    DeviceDetailsService deviceDetailsService;

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

    @GetMapping("/userPic/{userName}")
    public void getUserPic(@PathVariable("userName") String userName, HttpServletResponse response, HttpServletRequest request)
            throws ServletException, IOException, SQLException {

        User user = userRepository.findByUserName(userName);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(user.getPhoto().getBytes(1, (int) user.getPhoto().length()));

        response.getOutputStream().close();
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

        if (userService.checkIfUserNameBusy(user.getEmail())) {
            ModelAndView mv = new ModelAndView();
            user.setDateOfBirth(null);
            mv.addObject("user", user);
            mv.addObject("error", "This email is busy!");
            mv.setViewName("user_add");
            return mv;
        } else {
            user.setPhoto(filesService.getBlobData(photo));
            user.setAge();
            user.setName();
            UserGroup userGroup = userGroupRepository.findByCode("admin");
            user.setUserGroupName(userGroup);
            user.setUserName(user.getEmail());
            DeviceDetails deviceDetails = deviceDetailsService.findAll().get(0);
            user.setDeviceDetails(deviceDetails);

            User userNew = userService.saveUser(user);

            return new ModelAndView(new RedirectView(mContext.getContextPath() +"/users/list"));
        }
    }

    @GetMapping("/edit/{userName}")
    public ModelAndView getEditUser(@PathVariable("userName") String userName, Model model) {
        User user = userRepository.findByUserName(userName);
        ModelAndView mv = new ModelAndView();
        mv.addObject("user", user);
        mv.setViewName("user_edit");
        return mv;
    }

    @PostMapping("/edit/{userName}")
    public ModelAndView editUser(@RequestParam("photo") MultipartFile photo, @PathVariable("userName") String userName, @Valid User user, BindingResult result, Model model) throws IOException, SQLException {
        User userFromDB = userRepository.findByUserName(userName);
        user.setEmail(userName);

        if (!photo.isEmpty()) {
            user.setPhoto(filesService.getBlobData(photo));
        } else {
            user.setPhoto(userFromDB.getPhoto());
        }

        if (user.getDateOfBirth() == null) {
            user.setDateOfBirth(userFromDB.getDateOfBirth());
        }

        userService.updateUser(user);

        return new ModelAndView(new RedirectView(mContext.getContextPath() + "/users/list"));
    }

    @GetMapping("/view/{userName}")
    public ModelAndView getViewUser(@PathVariable("userName") String userName, Model model) {
        User user = userRepository.findByUserName(userName);
        ModelAndView mv = new ModelAndView();
        mv.addObject("user", user);
        mv.setViewName("user_view");
        return mv;
    }
}
