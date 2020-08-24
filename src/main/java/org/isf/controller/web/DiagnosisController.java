package org.isf.controller.web;

import org.isf.priaid.Diagnosis.Model.Gender;
import org.isf.priaid.Diagnosis.Model.HealthDiagnosis;
import org.isf.service.PriaidService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @RequestMapping(value = "/get_diagnosis", method = RequestMethod.GET)
    public @ResponseBody String getDiagnosisInfo(@RequestParam String symptom1, @RequestParam String symptom2,
                                                 @RequestParam String symptom3, @RequestParam String symptom4, @RequestParam String symptom5,
                                                 @RequestParam String yearOfBirth, @RequestParam String sex,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        String res = "";

        PriaidService priaidService = new PriaidService();
        priaidService.setClient();

        List<Integer> symptoms = new ArrayList<>();
        symptoms.add(Integer.valueOf(symptom1));
        symptoms.add(Integer.valueOf(symptom2));
        symptoms.add(Integer.valueOf(symptom3));
        symptoms.add(Integer.valueOf(symptom4));
        symptoms.add(Integer.valueOf(symptom5));

        symptoms.removeAll(Arrays.asList(0));

        Gender gender = null;

        if (sex.equals("M")) {
            gender = Gender.Male;
        } else {
            gender = Gender.Female;
        }

        List<HealthDiagnosis> healthDiagnoses = priaidService.getDiagnosisBySymptoms(symptoms, gender, Integer.parseInt(yearOfBirth));

        if (healthDiagnoses.isEmpty()) {
            res = "No diagnosis";
        }

        for (HealthDiagnosis healthDiagnosis : healthDiagnoses) {
            res = res.concat(healthDiagnosis.Issue.IcdName + " - " + healthDiagnosis.Issue.Name + System.lineSeparator());
        }

        return res;
    }


}

