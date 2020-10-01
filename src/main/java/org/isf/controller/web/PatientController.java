package org.isf.controller.web;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.isf.dao.*;
import org.isf.models.DiseaseModel;
import org.isf.models.ExaminationsModel;
import org.isf.models.PreviousVisitModel;
import org.isf.repository.UserRepository;
import org.isf.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

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

    @Autowired
    protected PathologyService pathologyService;

    @Autowired
    protected XLSXService xlsxService;

    @Autowired
    protected FilesService filesService;

    @Autowired
    protected DeviceDetailsService deviceDetailsService;

    @Autowired
    protected PDFService pdfService;

    @GetMapping(value = "/list")
    public ModelAndView getPatients(Model model) throws IOException, ParseException {
        Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(auth.getName());

        List<Patient> patients = patientService.findAll();

        for (Patient p : patients) {
            try {
                Examinations examinations = examinationService.getLastExaminationByPatient(p);
                ExaminationsModel examinationsModel = new ExaminationsModel(examinations);
                examinationsModel = examinationService.setExaminationColors(examinationsModel, p.getAge());

                if (examinationsModel.getScore() > 6) {
                    p.setPddScore("red");
                } else if (examinationsModel.getScore() > 4){
                    p.setPddScore("orange");
                } else if (examinationsModel.getScore() > 3){
                    p.setPddScore("yellow");
                } else {
                    p.setPddScore("white");
                }
            } catch (Exception e) {
                p.setPddScore("white");
            }

            try {
                Visit lastVisit = visitService.getLastVisitByPatient(p);
                if (lastVisit == null) {
                    p.setDateOfLastVisit("No visits");
                } else {
                    p.setDateOfLastVisit(lastVisit.getDate().toString().substring(0,10));
                }
            } catch (Exception e) {
                p.setDateOfLastVisit("No visits");
            }
        }

        if (patients.isEmpty()) patients = null;

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
        patient.setBlobPhoto(filesService.getBlobData(photo));
        patient.setExistingMedication(filesService.getBlobData(existingMedication));
        DeviceDetails deviceDetails = deviceDetailsService.findAll().get(0);
        patient.setDeviceDetails(deviceDetails);
        patient.setAge();

        Patient patientNew = patientService.savePatient(patient);

        return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/view/" + patientNew.getCode()));
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") String code, Model model) {
        patientService.deleteByCode(UUID.fromString(code));
        return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/list"));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditUser(@PathVariable("id") String code, Model model) {
        Patient patient = patientService.findPatientByCode(UUID.fromString(code));
        ModelAndView mv = new ModelAndView();
        mv.addObject("patient", patient);
        mv.setViewName("patient_edit");
        return mv;
    }


    @GetMapping("/view/{id}")
    public ModelAndView getViewUser(@PathVariable("id") String code, Model model) {
        Patient patient = patientService.findPatientByCode(UUID.fromString(code));
        ModelAndView mv = new ModelAndView();

        try {
            Examinations examinations = examinationService.getLastExaminationByPatient(patient);
            ExaminationsModel examinationsModel = new ExaminationsModel(examinations);
            examinationsModel = examinationService.setExaminationColors(examinationsModel, patient.getAge());
            if (examinationsModel.getScore() > 6) {
                patient.setPddScore("red");
            } else if (examinationsModel.getScore() > 4){
                patient.setPddScore("orange");
            } else if (examinationsModel.getScore() > 3){
                patient.setPddScore("yellow");
            } else {
                patient.setPddScore("white");
            }
        } catch (Exception e) {
            patient.setPddScore("white");
        }

        mv.addObject("patient", patient);
        mv.setViewName("patient_view");
        return mv;
    }

    @PostMapping("/edit")
    public ModelAndView editUser(@RequestParam("photo") MultipartFile photo, @RequestParam("existingMedication") MultipartFile existingMedication, @Valid Patient patient, BindingResult result, Model model) throws IOException, SQLException {
        Patient patientFromDB = patientService.findPatientByCode(patient.getCode());

        if (!photo.isEmpty()) {
            patient.setBlobPhoto(filesService.getBlobData(photo));
        } else {
            patient.setBlobPhoto(patientFromDB.getBlobPhoto());
        }

        if (!existingMedication.isEmpty()) {
            patient.setExistingMedication(filesService.getBlobData(existingMedication));
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
    public void getUserPic(@PathVariable("id") String code, HttpServletResponse response, HttpServletRequest request)
            throws ServletException, IOException, SQLException {

        Patient patient = patientService.findPatientByCode(UUID.fromString(code));
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(patient.getBlobPhoto().getBytes(1, (int) patient.getBlobPhoto().length()));

        response.getOutputStream().close();
    }


    @GetMapping("/visit/add/{id}")
    public ModelAndView getAddVisit(@PathVariable("id") String code, Model model) throws IOException, ParseException {
        try {
            Patient patient = patientService.findPatientByCode(UUID.fromString(code));
            ModelAndView mv = new ModelAndView();

            try {
                Examinations examinations = examinationService.getLastExaminationByPatient(patient);
                ExaminationsModel examinationsModel = new ExaminationsModel(examinations);
                examinationsModel = examinationService.setExaminationColors(examinationsModel, patient.getAge());
                if (examinationsModel.getScore() > 6) {
                    patient.setPddScore("red");
                } else if (examinationsModel.getScore() > 4){
                    patient.setPddScore("orange");
                } else if (examinationsModel.getScore() > 3){
                    patient.setPddScore("yellow");
                } else {
                    patient.setPddScore("white");
                }
            } catch (Exception e) {
                patient.setPddScore("white");
            }

            mv.addObject("patient", patient);
            mv.addObject("visit", new Visit());

            String yearOfBirth = patient.getBirthDate().toString().substring(0,4);
            mv.addObject("yearOfBirth", yearOfBirth);

            List<Visit> visits = visitService.findAllByPatient(patient);
            List<PreviousVisitModel> previousVisits = new ArrayList<>();

            for (Visit visit : visits) {
                PreviousVisitModel previousVisit = new PreviousVisitModel(visit);
                previousVisits.add(previousVisit);
            }

            if (previousVisits.isEmpty()) {
                previousVisits = null;
            }

            Locale locale = LocaleContextHolder.getLocale();
            String loc = locale.toString();

            mv.addObject("visits", previousVisits);
            java.util.List<String> symptomsList = csvService.getSymptomsList();
            mv.addObject("symptoms", symptomsList);
            java.util.List<DiseaseModel> diseasesList = xlsxService.getDiseasesList(loc);
            mv.addObject("diseases", diseasesList);
            Examinations examination = examinationService.getLastExaminationByPatient(patient);
            ExaminationsModel examinationsModel = new ExaminationsModel(examination);
            examinationsModel = examinationService.setExaminationColors(examinationsModel, patient.getAge());
            mv.addObject("examinations", examinationsModel);
            mv.addObject("openOnLastVisits", false);
            mv.setViewName("visit_add");
            return mv;
        } catch (Exception e) {
            Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUserName(auth.getName());

            List<Patient> patients = new ArrayList<Patient>();
            patients = patientService.findAll();

            for (Patient p : patients) {
                try {
                    Examinations examinations = examinationService.getLastExaminationByPatient(p);
                    ExaminationsModel examinationsModel = new ExaminationsModel(examinations);
                    examinationsModel = examinationService.setExaminationColors(examinationsModel, p.getAge());
                    if (examinationsModel.getScore() > 6) {
                        p.setPddScore("red");
                    } else if (examinationsModel.getScore() > 4){
                        p.setPddScore("orange");
                    } else if (examinationsModel.getScore() > 3){
                        p.setPddScore("yellow");
                    } else {
                        p.setPddScore("white");
                    }
                } catch (Exception f) {
                    p.setPddScore("white");
                }
            }

            ModelAndView mv = new ModelAndView();
            mv.addObject("userName", "Welcome " + user.getUserName());
            mv.addObject("patients", patients);
            mv.addObject("error", "Please add examinations in 'PDD' before adding visit for patient");
            mv.setViewName("patient_list");
            return mv;
        }
    }

    @PostMapping("/visit/add/{id}")
    public ModelAndView postAddVisit(@PathVariable("id") String code, @Valid Visit newVisit, BindingResult result, Model model) throws Exception {
        Patient patient = patientService.findPatientByCode(UUID.fromString(code));
        Examinations lastExamination = examinationService.getLastExaminationByPatient(patient);

        newVisit.setPatient(patient);
        newVisit.setExamination(lastExamination);

        Date date = new Date();
        newVisit.setDate(date);

        newVisit.defineMed();

        visitService.saveVisit(newVisit);

        ModelAndView mv = new ModelAndView();

        try {
            Examinations examinations = examinationService.getLastExaminationByPatient(patient);
            ExaminationsModel examinationsModel = new ExaminationsModel(examinations);
            examinationsModel = examinationService.setExaminationColors(examinationsModel, patient.getAge());
            if (examinationsModel.getScore() > 6) {
                patient.setPddScore("red");
            } else if (examinationsModel.getScore() > 4){
                patient.setPddScore("orange");
            } else if (examinationsModel.getScore() > 3){
                patient.setPddScore("yellow");
            } else {
                patient.setPddScore("white");
            }
        } catch (Exception e) {
            patient.setPddScore("white");
        }

        mv.addObject("patient", patient);
        mv.addObject("visit", new Visit());

        String yearOfBirth = patient.getBirthDate().toString().substring(0,4);
        mv.addObject("yearOfBirth", yearOfBirth);

        List<Visit> visits = visitService.findAllByPatient(patient);
        List<PreviousVisitModel> previousVisits = new ArrayList<>();

        for (Visit visit : visits) {
            PreviousVisitModel previousVisit = new PreviousVisitModel(visit);
            previousVisits.add(previousVisit);
        }

        if (previousVisits.isEmpty()) {
            previousVisits = null;
        }

        Locale locale = LocaleContextHolder.getLocale();
        String loc = locale.toString();

        mv.addObject("visits", previousVisits);
        java.util.List<String> symptomsList = csvService.getSymptomsList();
        mv.addObject("symptoms", symptomsList);
        java.util.List<DiseaseModel> diseasesList = xlsxService.getDiseasesList(loc);
        mv.addObject("diseases", diseasesList);
        Examinations examination = examinationService.getLastExaminationByPatient(patient);
        ExaminationsModel examinationsModel = new ExaminationsModel(examination);
        examinationsModel = examinationService.setExaminationColors(examinationsModel, patient.getAge());
        mv.addObject("examinations", examinationsModel);
        mv.addObject("openOnLastVisits", true);
        mv.setViewName("visit_add");
        return mv;
    }

    @GetMapping("/pdd/{id}")
    public ModelAndView getPdd(@PathVariable("id") String code, Model model) throws IOException, ParseException {

        try {
            Patient patient = patientService.findPatientByCode(UUID.fromString(code));
            ModelAndView mv = new ModelAndView();
            mv.addObject("patient", patient);
            List<Examinations> examinations = examinationService.getExaminations(patient);
            List<ExaminationsModel> examinationsModels = new ArrayList<>();

            for (Examinations exam : examinations) {
                ExaminationsModel examinationsModel = new ExaminationsModel(exam);
                examinationsModel = examinationService.setExaminationColors(examinationsModel, patient.getAge());
                examinationsModels.add(examinationsModel);
            }

            if (examinationsModels.isEmpty()) {
                examinationsModels = null;
            }

            List<Pathology> pathologies = pathologyService.getPathologies(patient);

            if (pathologies.isEmpty()) {
                pathologies = null;
            }

            mv.addObject("pathology", new Pathology());
            mv.addObject("pathologies", pathologies);
            mv.addObject("examinations", examinationsModels);
            mv.setViewName("pdd_list");
            return mv;
        } catch (Exception e) {
            return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/list"));
        }
    }

    @GetMapping("/pdd/add/{id}")
    public ModelAndView getAddPdd(@PathVariable("id") String code, Model model) throws IOException, ParseException {

        try {
            Patient patient = patientService.findPatientByCode(UUID.fromString(code));
            ModelAndView mv = new ModelAndView();
            mv.addObject("patient", patient);
            Examinations examinations = new Examinations();
            mv.addObject("examinations", examinations);
            mv.setViewName("pdd_add");
            return mv;
        } catch (Exception e) {
            return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/list"));
        }
    }

    @PostMapping("/pdd/add_report/{id}")
    public ModelAndView postAddPathology(@PathVariable("id") String code, @RequestParam("pathologyData") MultipartFile pathologyData, @Valid Pathology pathology, BindingResult result, Model model) throws IOException, SQLException {
        Patient patient = patientService.findPatientByCode(UUID.fromString(code));
        pathology.setPatient(patient);
        pathology.setPathologyData(filesService.getBlobData(pathologyData));

        Date date = new Date();
        pathology.setDate(date);

        pathologyService.savePathology(pathology);
        return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/pdd/" + code));
    }


    @PostMapping("/pdd/add/{id}")
    public ModelAndView postAddPdd(@PathVariable("id") String code, @Valid Examinations examinations, BindingResult result, Model model) throws IOException, SQLException {
        Patient patient = patientService.findPatientByCode(UUID.fromString(code));
        examinations.setPatient(patient);
        examinations.setId(null);

        Date date = new Date();
        examinations.setDate(date);

        examinationService.saveExaminations(examinations);

        return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/pdd/" + code));
    }

    @GetMapping("/examinations/{id}")
    public ModelAndView getExaminations(@PathVariable("id") String code, Model model) throws IOException, ParseException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUserName(auth.getName());

            Patient patient = patientService.findPatientByCode(UUID.fromString(code));
            ModelAndView mv = new ModelAndView();
            mv.addObject("patient", patient);
            mv.addObject("userName", "Welcome " + user.getUserName() +"!");
            mv.setViewName("examinations");
            return mv;
        } catch (Exception e) {
            return new ModelAndView(new RedirectView(mContext.getContextPath() +"/patient/list"));
        }
    }

    @RequestMapping("/pdd/get_report/{id}")
    public String downloadPddReport(@PathVariable("id") String id, HttpServletResponse response) {

        Pathology pathology = pathologyService.getPathology(UUID.fromString(id));
        try {
            response.setHeader("Content-Disposition", "inline; filename=\"" + id + "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            IOUtils.copy(pathology.getPathologyData().getBinaryStream(), out);
            out.flush();
            out.close();

        } catch (SQLException e) {
            System.out.println(e.toString());
            //Handle exception here
        } catch (IOException e) {
            System.out.println(e.toString());
            //Handle exception here
        }

        return "Success";
    }

    @RequestMapping("/visit/get_visit_report/{id}")
    public String downloadVisitReport(@PathVariable("id") String id, HttpServletResponse response) {

        Visit visit = visitService.getVisitById(UUID.fromString(id));
        try {
            File pdf = pdfService.createDocumentFromEntry(visit);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(pdf));
            pdf.delete();

            response.setHeader("Content-Disposition", "inline; filename=\"" + id + "\"");
            OutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            IOUtils.copy(resource.getInputStream(), out);
            out.flush();
            out.close();

        } catch (IOException e) {
            System.out.println(e.toString());
            //Handle exception here
        }

        return "Success";
    }

}
