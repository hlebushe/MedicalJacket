package org.isf.service;

import org.isf.dao.Patient;
import org.isf.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository repository;

    public List<Patient> findAll() {
        return repository.findAll();
    }

    public Patient savePatient(Patient patient) {
        return repository.save(patient);
    }

    public void deleteByCode(int code) {
        repository.deleteByCode(code);
    }

    public Patient findPatientByCode(int code) {
        return repository.findByCode(code);
    }

    public boolean updatePatient(Patient patient) {
        Patient patientFromDB = repository.findByCode(patient.getCode());

        if (patientFromDB == null) {
            return false;
        }

        patientFromDB.setFirstName(patient.getFirstName());
        patientFromDB.setSecondName(patient.getSecondName());
        patientFromDB.setBirthDate(patient.getBirthDate());
        patientFromDB.setAge(patient.getBirthDate());
        patientFromDB.setSex(patient.getSex());
        patientFromDB.setAddress(patient.getAddress());
        patientFromDB.setCity(patient.getCity());
        patientFromDB.setNextKin(patient.getNextKin());
        patientFromDB.setEmail(patient.getEmail());
        patientFromDB.setTelephone(patient.getTelephone());
        patientFromDB.setMother_name(patient.getMother_name());
        patientFromDB.setMother(patient.getMother());
        patientFromDB.setFather_name(patient.getFather_name());
        patientFromDB.setFather(patient.getFather());
        patientFromDB.setHasInsurance(patient.getHasInsurance());
        patientFromDB.setParentTogether(patient.getParentTogether());
        patientFromDB.setBloodType(patient.getBloodType());
        patientFromDB.setBlobPhoto(patient.getBlobPhoto());
        patientFromDB.setTaxCode(patient.getTaxCode());
        patientFromDB.setProfession(patient.getProfession());
        patientFromDB.setMaritalStatus(patient.getMaritalStatus());
        patientFromDB.setArea(patient.getArea());
        patientFromDB.setSmoker(patient.getSmoker());
        patientFromDB.setSmokingYears(patient.getSmokingYears());
        patientFromDB.setSmokingADay(patient.getSmokingADay());
        patientFromDB.setAlcohol(patient.getAlcohol());
        patientFromDB.setAlcoholWeek(patient.getAlcoholWeek());
        patientFromDB.setAlcoholYears(patient.getAlcoholYears());
        patientFromDB.setAllergies(patient.getAllergies());
        patientFromDB.setHealthCards(patient.getHealthCards());
        patientFromDB.setPreviousMedicalConditions(patient.getPreviousMedicalConditions());
        patientFromDB.setNatureOfMedicalCondition(patient.getNatureOfMedicalCondition());
        patientFromDB.setExistingMedication(patient.getExistingMedication());
        patientFromDB.setPreviousOperations(patient.getPreviousOperations());
        patientFromDB.setPreviousOperationNature1(patient.getPreviousOperationNature1());
        patientFromDB.setPreviousOperationYear1(patient.getPreviousOperationYear1());
        patientFromDB.setPreviousOperationNature2(patient.getPreviousOperationNature2());
        patientFromDB.setPreviousOperationYear2(patient.getPreviousOperationYear2());
        patientFromDB.setPreviousOperationNature3(patient.getPreviousOperationNature3());
        patientFromDB.setPreviousOperationYear3(patient.getPreviousOperationYear3());
        patientFromDB.setHereditaryDiseasesInFamily(patient.getHereditaryDiseasesInFamily());
        patientFromDB.setHealthCardNumber(patient.getHealthCardNumber());
        patientFromDB.setHealthCardType(patient.getHealthCardType());
        patientFromDB.setInsuranceCompany(patient.getInsuranceCompany());
        patientFromDB.setInsuranceNumber(patient.getInsuranceNumber());
        patientFromDB.setNextKinRelationship(patient.getNextKinRelationship());
        patientFromDB.setNextKinTelephone(patient.getNextKinTelephone());
        repository.save(patientFromDB);
        return true;
    }



}
