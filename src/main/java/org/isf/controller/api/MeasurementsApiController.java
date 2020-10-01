package org.isf.controller.api;

import org.isf.dao.Examinations;
import org.isf.dao.MeasurementTypes;
import org.isf.dao.MeasurementsData;
import org.isf.dao.Patient;
import org.isf.models.MeasurementModel;
import org.isf.repository.MeasurementTypeRepository;
import org.isf.repository.MeasurementsDataRepository;
import org.isf.service.ExaminationService;
import org.isf.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
