package org.isf.controller.web;

import org.isf.service.XLSXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequestMapping(value = "/medication")

@Controller
public class MedicationController {

    @Autowired
    XLSXService xlsxService;

    @RequestMapping(value = "/get_medications", method = RequestMethod.GET)
    public @ResponseBody
    List<String> getMedications(@RequestParam String diagnosis, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> res = new ArrayList<>();

        res = xlsxService.getMedications(diagnosis);

        return res;
    }

    @RequestMapping(value = "/get_combination", method = RequestMethod.GET)
    public @ResponseBody
    List<String> getCombination(@RequestParam String medication, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> res = new ArrayList<>();

        res = xlsxService.getCombination(medication);

        return res;
    }

}
