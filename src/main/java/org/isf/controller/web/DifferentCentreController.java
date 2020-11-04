package org.isf.controller.web;

import org.isf.dao.DifferentCentre;
import org.isf.service.DifferentCentreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/centre/")
public class DifferentCentreController {
    @Autowired
    private DifferentCentreService service;

    @GetMapping(value = "add")
    public String viewAdd(Model model) {
        model.addAttribute("differentCentre", new DifferentCentre());
        return "centre-add";
    }

    @PostMapping(value = "add")
    public String add(DifferentCentre differentCentre, BindingResult result, Model model) {
        if (result.hasFieldErrors("name")) {
            model.addAttribute("errorCode", "Name is required");
        }else if (result.hasFieldErrors("mobile")) {
            model.addAttribute("errorCode", "Email is required");
        } else if (result.hasFieldErrors("mobile")) {
            model.addAttribute("errorCode", "Mobile is required");
        } else if (result.hasFieldErrors("address")) {
            model.addAttribute("errorCode", "Address is required");
        } else if (service.existsByEmail(differentCentre.getEmail())) {
            model.addAttribute("errorCode", "Already exists, Please Try new one");
        } else {
            this.service.save(differentCentre);
            model.addAttribute("differentCentre", new DifferentCentre());
            model.addAttribute("successMsg", "Data Added Successfully");
        }
        return "centre-add";
    }

    @GetMapping("/edit/{email}")
    public String viewEdit(@PathVariable("email") String email, Model model) {
        model.addAttribute("differentCentre", this.service.findByEmail(email));
        return "centre-edit";
    }

    @PostMapping("/edit/{email}")
    public String editUser(@Valid DifferentCentre differentCentre, @PathVariable("email") String email, BindingResult result, Model model) {
        if (result.hasFieldErrors("name")) {
            model.addAttribute("errorCode", "Name is required");
        }else if (result.hasFieldErrors("mobile")) {
            model.addAttribute("errorCode", "Email is required");
        } else if (result.hasFieldErrors("mobile")) {
            model.addAttribute("errorCode", "Mobile is required");
        } else if (result.hasFieldErrors("address")) {
            model.addAttribute("errorCode", "Address is required");
        } else if (service.existsByEmail(differentCentre.getEmail())) {
            model.addAttribute("errorCode", "Already exists, Please Try new one");
        } else {
            DifferentCentre pathologyCentre1 = this.service.findByEmail(email);
            if (this.service.existsByEmail(email)) {
                pathologyCentre1.setAddress(differentCentre.getAddress());
                pathologyCentre1.setMobile(differentCentre.getMobile());
                pathologyCentre1.setName(differentCentre.getName());
                pathologyCentre1.setLogo(differentCentre.getLogo());
                this.service.update(pathologyCentre1);
                return "redirect:/centre/list";
            }
        }
        return "centre-edit";
    }

    @GetMapping("/delete/{email}")
    public String delete(@PathVariable("email") String email, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName() != null && auth.getName().equalsIgnoreCase("admin")) {
            this.service.deleteByEmail(email);
        } else {
            model.addAttribute("notallowed", "You don't have permission to delete this data");
        }
        return "redirect:/centre/list";
    }

    @GetMapping(value = "list")
    public String viewList(Model model) {
        model.addAttribute("list", this.service.findAll());
        return "centre-list";
    }

}
