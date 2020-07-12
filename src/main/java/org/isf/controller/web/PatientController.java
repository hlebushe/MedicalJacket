package org.isf.controller.web;

import org.isf.dao.Examinations;
import org.isf.dao.User;
import org.isf.models.ExaminationsModel;
import org.isf.dao.Patient;
import org.isf.models.PreviousVisitModel;
import org.isf.repository.UserRepository;
import org.isf.service.CSVService;
import org.isf.service.ExaminationService;
import org.isf.service.PatientService;
import org.isf.service.VisitService;
import org.isf.dao.Visit;
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
import javax.sql.rowset.serial.SerialBlob;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping(value = "/patient")

@Controller
public class PatientController {

    @Autowired
    protected ServletContext mContext;

    @Autowired
    protected PatientService patientService;

    @Autowired
    protected CSVService csvService;

    @Autowired
    protected ExaminationService examinationService;

    @Autowired
    protected VisitService visitService;

    @Autowired
    protected UserRepository userRepository;

    @GetMapping(value = "/list")
    public ModelAndView getPatients(Model model) {
        Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(auth.getName());

        List<Patient> patients = new ArrayList<Patient>();
        patients = patientService.findAll();

        ModelAndView mv = new ModelAndView();
        mv.addObject("userName", "Welcome " + user.getUserName());
        mv.addObject("patients", patients);
        mv.setViewName("patient_list");
        return mv;
    }

    @GetMapping(value = "/add")
    public ModelAndView getAddPatient(Model model) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("patient", new Patient());
        mv.setViewName("patient_add");
        return mv;
    }

    @PostMapping("/add")
    public ModelAndView addPatient(@RequestParam("photo") MultipartFile photo, @RequestParam("existingMedication") MultipartFile existingMedication, @Valid Patient patient, BindingResult result, Model model) throws IOException, SQLException {
        patient.setBlobPhoto(getBlobData(photo));
        patient.setExistingMedication(getBlobData(existingMedication));

        patient.setAge();

        patientService.savePatient(patient);

        return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/list"));
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") int code, Model model) {
        patientService.deleteByCode(code);
        return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/list"));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditUser(@PathVariable("id") int code, Model model) {
        Patient patient = patientService.findPatientByCode(code).get(0);
        ModelAndView mv = new ModelAndView();
        mv.addObject("patient", patient);
        mv.setViewName("patient_edit");
        return mv;
    }

    @PostMapping("/edit")
    public ModelAndView editUser(@RequestParam("photo") MultipartFile photo, @RequestParam("existingMedication") MultipartFile existingMedication, @Valid Patient patient, BindingResult result, Model model) throws IOException, SQLException {
        Patient patientFromDB = patientService.findPatientByCode(patient.getCode()).get(0);

        if (!photo.isEmpty()) {
            patient.setBlobPhoto(getBlobData(photo));
        } else {
            patient.setBlobPhoto(patientFromDB.getBlobPhoto());
        }

        if (!existingMedication.isEmpty()) {
            patient.setExistingMedication(getBlobData(existingMedication));
        } else {
            patient.setExistingMedication(patientFromDB.getExistingMedication());
        }

        if (patient.getBirthDate() == null) {
            patient.setBirthDate(patientFromDB.getBirthDate());
        }

        patientService.updatePatient(patient);

        return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/list"));
    }

    @GetMapping("/userPic/{id}")
    public void getUserPic(@PathVariable("id") int code, HttpServletResponse response, HttpServletRequest request)
            throws ServletException, IOException, SQLException {

        Patient patient = patientService.findPatientByCode(code).get(0);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(patient.getBlobPhoto().getBytes(1, (int) patient.getBlobPhoto().length()));

        response.getOutputStream().close();
    }


    @GetMapping("/visit/add/{id}")
    public ModelAndView getAddVisit(@PathVariable("id") int code, Model model) throws IOException, ParseException {
        try {
            Patient patient = patientService.findPatientByCode(code).get(0);
            ModelAndView mv = new ModelAndView();
            mv.addObject("patient", patient);
            mv.addObject("visit", new Visit());

            List<Visit> visits = visitService.findAllByPatient(patient);
            List<PreviousVisitModel> previousVisits = new ArrayList<>();

            for (Visit visit : visits) {
                PreviousVisitModel previousVisit = new PreviousVisitModel(visit);
                previousVisits.add(previousVisit);
            }

            mv.addObject("visits", previousVisits);
            java.util.List<String> symptomsList = csvService.getSymptomsList();
            mv.addObject("symptoms", symptomsList);
            java.util.List<String> diseasesList = csvService.getDiseasesList();
            mv.addObject("diseases", diseasesList);
            Examinations examination = examinationService.getExaminationByPatient(patient);
            ExaminationsModel examinationsModel = new ExaminationsModel(examination);
            examinationsModel = examinationService.setExaminationColors(examinationsModel, patient);
            mv.addObject("examinations", examinationsModel);
            mv.setViewName("visit_add");
            return mv;
        } catch (Exception e) {
            return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/list"));
        }
    }

    @PostMapping("/visit/add/{id}")
    public ModelAndView postAddVisit(@PathVariable("id") int code, @Valid Visit visit, BindingResult result, Model model) throws IOException, SQLException {
        Patient patient = patientService.findPatientByCode(code).get(0);
        Examinations examination = examinationService.getExaminationByPatient(patient);

        visit.setPatient(patient);
        visit.setExamination(examination);

        Date date = new Date();
        visit.setDate(date);

        visitService.saveVisit(visit);

        return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/list"));
    }

    @GetMapping("/examinations/add/{id}")
    public ModelAndView getAddExaminations(@PathVariable("id") int code, Model model) throws IOException, ParseException {

        try {
            Patient patient = patientService.findPatientByCode(code).get(0);
            ModelAndView mv = new ModelAndView();
            mv.addObject("patient", patient);
            Examinations examinations = new Examinations();
            mv.addObject("examinations", examinations);
            mv.setViewName("examinations_add");
            return mv;
        } catch (Exception e) {
            return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/list"));
        }
    }

    @PostMapping("/examinations/add/{id}")
    public ModelAndView postAddVExaminations(@PathVariable("id") int code, @Valid Examinations examinations, BindingResult result, Model model) throws IOException, SQLException {
        Patient patient = patientService.findPatientByCode(code).get(0);
        examinations.setPatient(patient);
        examinationService.saveExaminaions(examinations);

        return new ModelAndView(new RedirectView(mContext.getContextPath() +"/home"));
    }



    public Blob getBlobData(MultipartFile file) throws IOException, SQLException {
        byte[] bytes = file.getBytes();
        return new SerialBlob(bytes);
    }
}
