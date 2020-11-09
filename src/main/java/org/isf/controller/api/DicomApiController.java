package org.isf.controller.api;

import org.isf.dao.NursingStationData;
import org.isf.dao.Patient;
import org.isf.dao.User;
import org.isf.dao.Visit;
import org.isf.models.ExaminationsModel;
import org.isf.models.TaskModel;
import org.isf.service.JSONService;
import org.isf.service.PatientService;
import org.isf.service.response.PathologyReport;
import org.isf.service.response.RadiologyReport;
import org.isf.util.TasksListComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/dicom")
public class DicomApiController {

    @Autowired
    JSONService jsonService;

    @Autowired
    PatientService patientService;

    @GetMapping("/pathology/{patientId}")
    public ModelAndView getPathology(@PathVariable("patientId") String code) throws IOException, ParseException {

        Patient patient = patientService.findPatientByCode(UUID.fromString(code));
        ModelAndView mv= new ModelAndView("pdd_list :: #pathologiesDicom");
        List<PathologyReport> pathologyDicom = jsonService.getPathologyByAadhaarId(patient.getAadhaarId());
        if (pathologyDicom.isEmpty()) {
            pathologyDicom = null;
        }

        mv.addObject("pathologiesDicom", pathologyDicom);
        return mv;
    }

    @GetMapping("/radiology/{patientId}")
    public ModelAndView getRadiology(@PathVariable("patientId") String code) throws IOException, ParseException {

        Patient patient = patientService.findPatientByCode(UUID.fromString(code));
        ModelAndView mv= new ModelAndView("pdd_list :: #radiologies");
        List<RadiologyReport> radiology = jsonService.getRadiologyByAadhaarId(patient.getAadhaarId());
        if (radiology.isEmpty()) {
            radiology = null;
        }

        mv.addObject("radiologies", radiology);
        return mv;
    }
}
