package org.isf.controller.api;

import org.isf.dao.*;
import org.isf.models.MeasurementModel;
import org.isf.repository.MeasurementTypeRepository;
import org.isf.repository.MeasurementsDataRepository;
import org.isf.service.ExaminationService;
import org.isf.service.FilesService;
import org.isf.service.PathologyService;
import org.isf.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
public class MeasurementsApiController {

    @Autowired
    PatientService patientService;

    @Autowired
    MeasurementTypeRepository measurementTypeRepository;

    @Autowired
    ExaminationService examinationService;

    @Autowired
    MeasurementsDataRepository measurementsDataRepository;

    @Autowired
    PathologyService pathologyService;

    @Autowired
    FilesService filesService;


    @RequestMapping(value = "/send_examination/{patientId}", method = RequestMethod.POST)
    public MeasurementModel sendMeasurement(@RequestBody MeasurementModel data, @PathVariable String patientId) throws ParseException {
        Patient patient = patientService.findPatientByCode(UUID.fromString(patientId));
        MeasurementTypes measurementTypes = measurementTypeRepository.findByIndex(data.getIndex());

        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.ENGLISH);
        Date date = formatter.parse(data.getDate());
        MeasurementsData measurementsData = new MeasurementsData();
        measurementsData.setMeasurementType(measurementTypes);
        measurementsData.setPatient(patient);
        measurementsData.setValue(data.getValue());
        measurementsData.setDate(date);
        measurementsDataRepository.save(measurementsData);
        examinationService.saveToExamination(data, patient, date);
        return data;

    }

    @RequestMapping(value="/upload_pathology_report/{aadharid}", method=RequestMethod.POST)
    public ResponseEntity uploadPathologyReport(@RequestParam("report") MultipartFile report, @PathVariable("aadharid") String aadharid) throws IOException, SQLException {
        try {
            Patient patient = patientService.getByAadharId(aadharid);

            Pathology pathology = new Pathology();
            pathology.setPatient(patient);
            pathology.setPathologyData(filesService.getBlobData(report));

            Date date = new Date();
            pathology.setDate(date);

            pathologyService.savePathology(pathology);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
