package org.isf.controller.web;

import org.isf.dao.Patient;
import org.isf.dao.NursingStationData;
import org.isf.dao.User;
import org.isf.dao.Visit;
import org.isf.models.ExaminationsModel;
import org.isf.repository.UserRepository;
import org.isf.service.ExaminationService;
import org.isf.service.NursingStationDataService;
import org.isf.service.PatientService;
import org.isf.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/nursing_station")

@Controller
public class NursingStationController {

    @Autowired
    protected ServletContext mContext;

    @Autowired
    protected PatientService patientService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ExaminationService examinationService;

    @Autowired
    protected NursingStationDataService nursingStationDataService;

    @Autowired
    protected VisitService visitService;

    @GetMapping(value = "/list")
    public ModelAndView getPatients(Model model) throws IOException, ParseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(auth.getName());

        List<Patient> patients = nursingStationDataService.getPatientsWithLastMeasurements();
        List<NursingStationData> patientMeasurements = new ArrayList<>();

        for (Patient patient : patients) {
            NursingStationData patientMeasurement = nursingStationDataService.getLastByPatient(patient);
            patientMeasurements.add(patientMeasurement);
        }

        List<ExaminationsModel> measurementsInfo = new ArrayList<>();

        for (NursingStationData pM : patientMeasurements) {
            ExaminationsModel examinations = new ExaminationsModel(pM);

            Visit lastVisit = visitService.getLastVisitByPatient(pM.getPatient());
            if (lastVisit != null) {
                examinations.setVisitData(lastVisit);
            }

            examinations = examinationService.setExaminationColors(examinations, Integer.parseInt(examinations.getPatientAge()));
            measurementsInfo.add(examinations);
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("userName", "Welcome " + user.getUserName());
        mv.addObject("measurements", measurementsInfo);
        mv.setViewName("nursing_station");
        return mv;
    }

    @GetMapping("/get_measurements")
    public ModelAndView updateData(Model model) throws IOException, ParseException {
        List<Patient> patients = nursingStationDataService.getPatientsWithLastMeasurements();
        List<NursingStationData> patientMeasurements = new ArrayList<>();

        for (Patient patient : patients) {
            NursingStationData patientMeasurement = nursingStationDataService.getLastByPatient(patient);
            patientMeasurements.add(patientMeasurement);
        }

        List<ExaminationsModel> measurementsInfo = new ArrayList<>();

        for (NursingStationData pM : patientMeasurements) {
            ExaminationsModel examinations = new ExaminationsModel(pM);

            Visit lastVisit = visitService.getLastVisitByPatient(pM.getPatient());
            if (lastVisit != null) examinations.setVisitData(lastVisit);

            examinations = examinationService.setExaminationColors(examinations, Integer.parseInt(examinations.getPatientAge()));
            measurementsInfo.add(examinations);
        }

        ModelAndView mv= new ModelAndView("nursing_station :: #measurements");
        mv.addObject("measurements", measurementsInfo);

        return mv;
    }

}
