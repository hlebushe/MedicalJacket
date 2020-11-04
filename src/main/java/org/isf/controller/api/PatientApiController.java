//package org.isf.controller.api;
//
//import io.swagger.annotations.*;
//import org.isf.patient.model.Patient;
//import org.isf.patient.service.PatientIoOperationRepository;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@Api(tags = "Patient API", description = "Patient")
//@RequestMapping(value = "/v1/api/patient")
//public class PatientApiController {
//	private final PatientIoOperationRepository repository;
//
//	public PatientApiController(PatientIoOperationRepository repository) {
//		this.repository = repository;
//	}
//
//	@ApiOperation(value = "Retrieve all patients")
//	@ApiResponses(value = {
//		@ApiResponse(code = 200, message = "Patient Data", response = Patient.class, responseContainer = "List"),
//		@ApiResponse(code = 500, message = "Internal Server Error")
//	})
//	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//	public List<Patient> findAll() {
//		return repository.findAll();
//	}
//
//	@ApiOperation(value = "Retrieve patient by id")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "id", value = "Patient Id", dataType = "int", paramType = "path", required = true, example = "0")
//	})
//	@ApiResponses(value = {
//		@ApiResponse(code = 200, message = "Patient Data", response = Patient.class),
//		@ApiResponse(code = 500, message = "Internal Server Error")
//	})
//	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Patient> findById(@PathVariable Integer id) {
//		Patient result = repository.findById(id).orElse(null);
//		return result != null ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	}
//
//	@ApiOperation(value = "Create/Update Patient")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "id", value = "Patient Id. Pass '0' for create action", dataType = "int", paramType = "path", required = true, example = "0")
//	})
//	@ApiResponses(value = {
//		@ApiResponse(code = 200, message = "Patient Updated"),
//		@ApiResponse(code = 201, message = "Patient Created"),
//		@ApiResponse(code = 500, message = "Internal Server Error")
//	})
//	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> save(@RequestBody Patient request, @PathVariable int id) {
//		HttpStatus status = HttpStatus.OK;
//		id = id > 0 ? id : -1; //0 is reserved
//		Patient record = repository.findById(id).orElse(null);
//		if (record == null) {
//			record = new Patient();
//			status = HttpStatus.CREATED;
//		}
//		record.setFirstName(request.getFirstName());
//		record.setSecondName(request.getSecondName());
//		record.setBirthDate(request.getBirthDate());
//		record.setAge(request.getAge());
//		record.setAgetype(request.getAgetype());
//		record.setSex(request.getSex());
//		record.setAddress(request.getAddress());
//		record.setCity(request.getCity());
//		record.setNextKin(request.getNextKin());
//		record.setTelephone(request.getTelephone());
//		record.setNote(request.getNote());
//		record.setMother_name(request.getMother_name());
//		record.setMother(request.getMother());
//		record.setFather_name(request.getFather_name());
//		record.setFather(request.getFather());
//		record.setBloodType(request.getBloodType());
//		record.setHasInsurance(request.getHasInsurance());
//		record.setParentTogether(request.getParentTogether());
//		record.setTaxCode(request.getTaxCode());
//		record.setMaritalStatus(request.getMaritalStatus());
//		record.setProfession(request.getProfession());
//		repository.save(record);
//		return new ResponseEntity<>(status);
//	}
//}
