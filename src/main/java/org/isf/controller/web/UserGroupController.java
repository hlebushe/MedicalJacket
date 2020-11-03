package org.isf.controller.web;

import org.isf.dao.DeviceDetails;
import org.isf.dao.User;
import org.isf.dao.UserGroup;
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

@RequestMapping(value = "/usergroup/")
@Controller
public class UserGroupController {

    @Autowired
    UserGroupRepository userGroupRepository;


    @GetMapping(value = "add")
    public String viewAdd(Model model) {
        model.addAttribute("userGroup", new UserGroup());
        return "user_group_add";
    }

    @PostMapping(value = "add")
    public String add(UserGroup userGroup, BindingResult result, Model model) {
        if (result.hasFieldErrors("code")) {
            model.addAttribute("enterCode", "Code is required");
        } else if (userGroupRepository.existsByCode(userGroup.getCode())) {
            model.addAttribute("alreadyExist", "Already exists, Please Try new one");
        } else {
            this.userGroupRepository.save(userGroup);
            model.addAttribute("userGroup", new UserGroup());
            model.addAttribute("successMsg", "User Group Added Successfully");
        }
        return "user_group_add";
    }

    @GetMapping("/edit/{code}")
    public String viewEdit(@PathVariable("code") String code, Model model) {
        model.addAttribute("userGroup", this.userGroupRepository.findByCode(code));
        return "user_group_edit";
    }

    @PostMapping("/edit/{code}")
    public String editUser(@Valid UserGroup userGroup, @PathVariable("code") String code, BindingResult result, Model model) {
        UserGroup userGroup1 = this.userGroupRepository.findByCode(code);
        if (this.userGroupRepository.existsByCode(code)) {
            userGroup1.setDesc(userGroup.getDesc());
            this.userGroupRepository.save(userGroup1);
            return "redirect:/usergroup/list";
        }else if(userGroup.getDesc().isEmpty() || userGroup.getDesc() == null){
            model.addAttribute("errMsg","Enter Description");
        }
        return "user_group_edit";
    }

    @GetMapping("/delete/{code}")
    public String delete(@PathVariable("code") String code, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getName() != null && auth.getName().equalsIgnoreCase("admin")){
            this.userGroupRepository.deleteByCode(code);
        }else {
            model.addAttribute("notallowed", "You don't have permission to delete this data");
        }

        return "redirect:/usergroup/list";
    }
    @GetMapping(value = "list")
    public String viewList(Model model) {
        model.addAttribute("list", this.userGroupRepository.findAll());
        return "user_group_list";
    }

}
