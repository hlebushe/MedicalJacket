package org.isf.controller.web;

import org.isf.service.XLSXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping(value = "/diagnosis")

@Controller
public class DiagnosisController {

    @Autowired
    XLSXService xlsxService;

    @RequestMapping(value = "/get_symptom_info", method = RequestMethod.GET)
    public @ResponseBody String getSymptomInfo(@RequestParam String symptom1, @RequestParam String infoType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String res = "";

        if (!symptom1.isEmpty()) {
            res = res.concat(xlsxService.getSymptomInfo(symptom1, infoType));
        }

        return res;
    }


    @RequestMapping(value = "/get_symptoms_info", method = RequestMethod.GET)
    public @ResponseBody String getSymptomsInfo(@RequestParam String symptom1, @RequestParam String symptom2, @RequestParam String symptom3, @RequestParam String symptom4, @RequestParam String symptom5, @RequestParam String infoType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String res = "";

        if (!symptom1.isEmpty()) {
            res = res.concat(symptom1 + ": " + xlsxService.getSymptomInfo(symptom1, infoType) + System.lineSeparator() + System.lineSeparator());
        }

        if (!symptom2.isEmpty()) {
            res = res.concat(symptom2 + ": " + xlsxService.getSymptomInfo(symptom2, infoType) + System.lineSeparator() + System.lineSeparator());
        }

        if (!symptom3.isEmpty()) {
            res = res.concat(symptom3 + ": " + xlsxService.getSymptomInfo(symptom3, infoType) + System.lineSeparator() + System.lineSeparator());
        }

        if (!symptom4.isEmpty()) {
            res = res.concat(symptom4 + ": " + xlsxService.getSymptomInfo(symptom4, infoType) + System.lineSeparator() + System.lineSeparator());
        }

        if (!symptom5.isEmpty()) {
            res = res.concat(symptom5 + ": " + xlsxService.getSymptomInfo(symptom5, infoType) + System.lineSeparator() + System.lineSeparator());
        }

        return res;
    }

}

