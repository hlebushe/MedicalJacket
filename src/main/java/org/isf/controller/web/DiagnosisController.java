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

    @RequestMapping(value = "/get_symptoms_info", method = RequestMethod.GET)
    public @ResponseBody String getSymptomsInfo(@RequestParam String diagnosis1, @RequestParam String diagnosis2, @RequestParam String diagnosis3, @RequestParam String infoType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String res = "";

        if (!diagnosis1.isEmpty()) {
            res = res.concat(diagnosis1 + ": " + xlsxService.getSymptomInfo(diagnosis1, infoType) + System.lineSeparator() + System.lineSeparator());
        }

        if (!diagnosis2.isEmpty()) {
            res = res.concat(diagnosis2 + ": " + xlsxService.getSymptomInfo(diagnosis2, infoType) + System.lineSeparator() + System.lineSeparator());
        }

        if (!diagnosis3.isEmpty()) {
            res = res.concat(diagnosis3 + ": " + xlsxService.getSymptomInfo(diagnosis3, infoType) + System.lineSeparator() + System.lineSeparator());
        }

        return res;
    }


}

