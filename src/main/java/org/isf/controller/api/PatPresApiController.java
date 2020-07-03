//package org.isf.controller.api;
//
//import io.swagger.annotations.*;
//import org.isf.patient.model.Patient;
//import org.isf.patient.service.PatientIoOperationRepository;
//import org.isf.patpres.model.PatientPresentation;
//import org.isf.patpres.service.PatPresIoOperationRepository;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.Date;
//import java.util.List;
//
//@RestController
//@RequestMapping("/v1/api/patpres")
//@Api(tags = "Patient Presentation API", description = "Patient Presentation")
//public class PatPresApiController {
//	private final PatPresIoOperationRepository patPresRepo;
//	private final PatientIoOperationRepository patientRepo;
//
//	public PatPresApiController(PatPresIoOperationRepository patPresRepo, PatientIoOperationRepository patientRepo) {
//		this.patPresRepo = patPresRepo;
//		this.patientRepo = patientRepo;
//	}
//
//	@ApiOperation(value = "Retrieve all patient presentation records")
//	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//	public List<PatientPresentation> findAll() {
//		return patPresRepo.findAll();
//	}
//
//	@ApiOperation(value = "Retrieve patient presentation by id")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "id", value = "Patient Presentation Id", dataType = "int", paramType = "path", required = true, example = "0")
//	})
//	@ApiResponses(value = {
//		@ApiResponse(code = 200, message = "Patient Presentation Data", response = PatientPresentation.class),
//		@ApiResponse(code = 500, message = "Internal Server Error")
//	})
//	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<PatientPresentation> findById(@PathVariable Integer id) {
//		PatientPresentation result = patPresRepo.findById(id).orElse(null);
//		return result != null ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	}
//
//	@ApiOperation(value = "Retrieve patient presentation by patient's id")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "code", value = "Patient Id", dataType = "int", paramType = "path", required = true, example = "0")
//	})
//	@ApiResponses(value = {
//		@ApiResponse(code = 200, message = "Patient Presentation Data", response = PatientPresentation.class, responseContainer = "List"),
//		@ApiResponse(code = 500, message = "Internal Server Error")
//	})
//	@GetMapping(value = "/patient/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<List<PatientPresentation>> findByPatientCode(@PathVariable int code) {
//		List<PatientPresentation> result = patPresRepo.findByPatientCode(code);
//		return result != null ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	}
//
//	@ApiOperation(value = "Create/Update Patient Presentation")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "id", value = "Patient Presentation Id. Pass '0' for create action", dataType = "int", paramType = "path", required = true, example = "0")
//	})
//	@ApiResponses(value = {
//		@ApiResponse(code = 200, message = "Patient Presentation Updated"),
//		@ApiResponse(code = 201, message = "Patient Presentation Created"),
//		@ApiResponse(code = 500, message = "Internal Server Error")
//	})
//	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> save(@RequestBody PatientPresentation request, @PathVariable int id) {
//		HttpStatus status = HttpStatus.OK;
//		id = id > 0 ? id : -1; //0 is reserved
//		PatientPresentation record = patPresRepo.findById(id).orElse(null);
//		if (record == null) {
//			Patient patient = findPatient(request);
//			record = new PatientPresentation();
//			record.setPatient(patient);
//			status = HttpStatus.CREATED;
//		}
//
//		record.setVitals(request.getVitals());
//		record.setPresentationDate(request.getPresentationDate());
//		record.setConsultationEnd(request.getConsultationEnd());
//		record.setPreviousConsult(request.getPreviousConsult());
//		record.setReferredFrom(request.getReferredFrom());
//		record.setPatientAilmentDescription(request.getPatientAilmentDescription());
//		record.setDoctorsAilmentDescription(request.getDoctorsAilmentDescription());
//		record.setSpecificSymptoms(request.getSpecificSymptoms());
//		record.setDiagnosis(request.getDiagnosis());
//		record.setPrognosis(request.getPrognosis());
//		record.setPatientAdvice(request.getPatientAdvice());
//		record.setPrescribed(request.getPrescribed());
//		record.setFollowUp(request.getFollowUp());
//		record.setReferredTo(request.getReferredTo());
//		record.setSummary(request.getSummary());
//		patPresRepo.save(record);
//		return new ResponseEntity<>(status);
//	}
//
//	private Patient findPatient(PatientPresentation request) {
//		Patient patient = request.getPatient();
//		if (patient == null)
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No patient fields provided");
//
//		Integer code = patient.getCode();
//		String firstName = patient.getFirstName();
//		String secondName = patient.getSecondName();
//		Date birthDate = patient.getBirthDate();
//		char sex = patient.getSex();
//
//		// Find by code if provided
//		if (code != null && code > 0) {
//			patient = patientRepo.findById(code).orElse(null);
//			if (patient != null)
//				return patient;
//		}
//
//		if (StringUtils.isEmpty(firstName) || StringUtils.isEmpty(secondName) || birthDate == null || sex == 0)
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough patient fields provided. Either one is empty: first name, second name, birth date, sex");
//
//		// Otherwise find by combination of fields
//		List<Patient> patients = patientRepo.findAllByFilter(firstName, secondName, birthDate, sex);
//		if (patients == null || patients.size() == 0)
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No patients found matching the data");
//
//		if (patients.size() > 1)
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "More than one patients found for given data");
//
//		return patients.get(0);
//	}
//}
