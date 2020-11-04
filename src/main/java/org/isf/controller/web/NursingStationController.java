package org.isf.controller.web;

import org.isf.dao.Patient;
import org.isf.dao.NursingStationData;
import org.isf.dao.User;
import org.isf.dao.Visit;
import org.isf.models.ExaminationsModel;
import org.isf.models.TaskModel;
import org.isf.repository.UserRepository;
import org.isf.service.ExaminationService;
import org.isf.service.NursingStationDataService;
import org.isf.service.PatientService;
import org.isf.service.VisitService;
import org.isf.util.TasksListComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

        List<Patient> patients = nursingStationDataService.getPatientsWithLastMeasurementsByMachineId(user.getDeviceDetails().getMachineID());
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

            examinations.setDripFlow(pM.getDripFlow());

            //TODO refactor and beautify tasks parser
            String futureTasks = pM.getFutureTasks();

            if (futureTasks != null && !futureTasks.isEmpty()) {
                    String[] split = futureTasks.split(";");
                    List<TaskModel> futureTasksList = new ArrayList<>();

                    for (int i = 0; i < split.length; i++) {
                            TaskModel futureTask = new TaskModel();
                            futureTask.setTaskName(split[i].split("-")[0]);
                            futureTask.setTaskTime(split[i].split("-")[1]);
                            futureTasksList.add(futureTask);
                    }
                    Collections.sort(futureTasksList, new TasksListComparator());

                    List<String> futureTasksString = new ArrayList<>();

                    for (TaskModel t : futureTasksList) {
                        futureTasksString.add(t.getTaskString());
                    }

                    examinations.setTasksFuture(futureTasksString);
            }

            String pastTasks = pM.getPastTasks();

            if (pastTasks != null && !pastTasks.isEmpty()) {
                    String[] split = pastTasks.split(";");
                    List<TaskModel> pastTasksList = new ArrayList<>();

                        for (int i = 0; i < split.length; i++) {
                            TaskModel pastTask = new TaskModel();
                            pastTask.setTaskName(split[i].split("-")[0]);
                            pastTask.setTaskTime(split[i].split("-")[1]);
                            pastTasksList.add(pastTask);
                        }

                    Collections.sort(pastTasksList, new TasksListComparator());

                    List<String> pastTasksString = new ArrayList<>();

                    for (TaskModel t : pastTasksList) {
                        pastTasksString.add(t.getTaskString());
                    }

                    examinations.setTasksPast(pastTasksString);
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(auth.getName());
        List<Patient> patients = nursingStationDataService.getPatientsWithLastMeasurementsByMachineId(user.getDeviceDetails().getMachineID());
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

            examinations.setDripFlow(pM.getDripFlow());

            //TODO refarctor and beautify tasks parser
            String futureTasks = pM.getFutureTasks();

            if (futureTasks != null && !futureTasks.isEmpty()) {
                    String[] split = futureTasks.split(";");
                    List<TaskModel> futureTasksList = new ArrayList<>();

                    for (int i = 0; i < split.length; i++) {
                        TaskModel futureTask = new TaskModel();
                        futureTask.setTaskName(split[i].split("-")[0]);
                        futureTask.setTaskTime(split[i].split("-")[1]);
                        futureTasksList.add(futureTask);
                    }

                    Collections.sort(futureTasksList, new TasksListComparator());

                    List<String> futureTasksString = new ArrayList<>();

                    for (TaskModel t : futureTasksList) {
                        futureTasksString.add(t.getTaskString());
                    }

                    examinations.setTasksFuture(futureTasksString);
            }

            String pastTasks = pM.getPastTasks();

            if (pastTasks != null && !pastTasks.isEmpty()) {
                    String[] split = pastTasks.split(";");
                    List<TaskModel> pastTasksList = new ArrayList<>();

                        for (int i = 0; i < split.length; i++) {
                            TaskModel pastTask = new TaskModel();
                            pastTask.setTaskName(split[i].split("-")[0]);
                            pastTask.setTaskTime(split[i].split("-")[1]);
                            pastTasksList.add(pastTask);
                        }

                    Collections.sort(pastTasksList, new TasksListComparator());

                    List<String> pastTasksString = new ArrayList<>();

                    for (TaskModel t : pastTasksList) {
                        pastTasksString.add(t.getTaskString());
                    }

                    examinations.setTasksPast(pastTasksString);
            }

            examinations = examinationService.setExaminationColors(examinations, Integer.parseInt(examinations.getPatientAge()));
            measurementsInfo.add(examinations);
        }

        ModelAndView mv= new ModelAndView("nursing_station :: #measurements");
        mv.addObject("measurements", measurementsInfo);

        return mv;
    }

    @RequestMapping(value = "/confirm_task", method = RequestMethod.GET)
    public @ResponseBody
    String confirmTask(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID uid = UUID.fromString(id);

        NursingStationData nursingStationData = nursingStationDataService.findById(uid);

        String futureTasks = nursingStationData.getFutureTasks();

        if (futureTasks != null && !futureTasks.isEmpty()) {
                String[] split = futureTasks.split(";");
                List<TaskModel> futureTasksList = new ArrayList<>();

                for (int i = 0; i < split.length; i++) {
                    TaskModel futureTask = new TaskModel();
                    futureTask.setTaskName(split[i].split("-")[0]);
                    futureTask.setTaskTime(split[i].split("-")[1]);
                    futureTasksList.add(futureTask);
                }
                Collections.sort(futureTasksList, new TasksListComparator());
                TaskModel taskDone = futureTasksList.get(0);
                futureTasksList.remove(0);

                List<String> futTasks = new ArrayList<>();
                for (TaskModel t : futureTasksList) {
                    futTasks.add(t.getTaskStringForDb());
                }

                String futureTasksForDB = String.join(";", futTasks);

                String pastTasks = nursingStationData.getPastTasks();

                String pastTasksForDB = "";

                if (pastTasks != null && !pastTasks.isEmpty()) {
                    pastTasksForDB = pastTasks + ";" + taskDone.getTaskStringForDb();
                } else {
                    pastTasksForDB = taskDone.getTaskStringForDb();
                }


                nursingStationDataService.updateNursingStationDataTasks(uid, futureTasksForDB, pastTasksForDB);

        }

        return "";
    }

    @RequestMapping(value = "/undo_task", method = RequestMethod.GET)
    public @ResponseBody
    String undoTask(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID uid = UUID.fromString(id);

        NursingStationData nursingStationData = nursingStationDataService.findById(uid);

        String pastTasks = nursingStationData.getPastTasks();

        if (pastTasks != null && !pastTasks.isEmpty()) {
            String[] split = pastTasks.split(";");
            List<TaskModel> pastTasksList = new ArrayList<>();

            for (int i = 0; i < split.length; i++) {
                TaskModel pastTask = new TaskModel();
                pastTask.setTaskName(split[i].split("-")[0]);
                pastTask.setTaskTime(split[i].split("-")[1]);
                pastTasksList.add(pastTask);
            }
            Collections.sort(pastTasksList, new TasksListComparator());
            TaskModel taskToUndo = pastTasksList.get(pastTasksList.size()-1);
            pastTasksList.remove(pastTasksList.size()-1);

            List<String> paTasks = new ArrayList<>();
            for (TaskModel t : pastTasksList) {
                paTasks.add(t.getTaskStringForDb());
            }

            String pastTasksForDB = String.join(";", paTasks);

            String futureTasks = nursingStationData.getFutureTasks();

            String futureTasksForDB = "";

            if (futureTasks != null && !futureTasks.isEmpty()) {
                futureTasksForDB = futureTasks + ";" + taskToUndo.getTaskStringForDb();
            } else {
                futureTasksForDB = taskToUndo.getTaskStringForDb();
            }



            nursingStationDataService.updateNursingStationDataTasks(uid, futureTasksForDB, pastTasksForDB);

        }

        return "";
    }

}
