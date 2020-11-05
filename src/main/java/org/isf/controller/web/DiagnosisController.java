package org.isf.controller.web;

import org.isf.service.JSONService;
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

@RequestMapping(value = "/diagnosis")

@Controller
public class DiagnosisController {

    @Autowired
    XLSXService xlsxService;

    @Autowired
    JSONService jsonService;

    @RequestMapping(value = "/get_symptoms_info", method = RequestMethod.GET)
    public @ResponseBody List<String> getSymptomsInfo(@RequestParam String diagnosis1, @RequestParam String diagnosis2, @RequestParam String diagnosis3, @RequestParam String infoType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> res = new ArrayList<>();

        Locale locale = LocaleContextHolder.getLocale();
        String loc = locale.toString();

        if (!diagnosis1.isEmpty()) {
            res.add(diagnosis1 + ": " + xlsxService.getSymptomInfo(diagnosis1, infoType, loc) + System.lineSeparator() + System.lineSeparator());
        } else {
            res.add("");
        }

        if (!diagnosis2.isEmpty()) {
            res.add(diagnosis2 + ": " + xlsxService.getSymptomInfo(diagnosis2, infoType, loc) + System.lineSeparator() + System.lineSeparator());
        } else {
            res.add("");
        }

        if (!diagnosis3.isEmpty()) {
            res.add(diagnosis3 + ": " + xlsxService.getSymptomInfo(diagnosis3, infoType, loc) + System.lineSeparator() + System.lineSeparator());
        } else {
            res.add("");
        }

        return res;
    }

    @RequestMapping(value = "/get_diagnosis", method = RequestMethod.GET)
    public @ResponseBody List<List<String>> getDiagnosis(@RequestParam String symptoms, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<List<String>> res = new ArrayList<>();

        res = jsonService.getDiagnosis(symptoms);
        return res;
    }


}

