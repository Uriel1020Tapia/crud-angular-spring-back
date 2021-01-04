package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		return ResponseEntity.ok(employeeRepository.findAll());
	}

	@GetMapping("/misEmployees")
	public ResponseEntity<Map<String, Object>> getAllMisEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {

		try {
			List<Employee> employees = new ArrayList<Employee>();
			Pageable paging = PageRequest.of(page, size);

			Page<Employee> pageEmployees;

			pageEmployees = employeeRepository.findAll(paging);
			employees = pageEmployees.getContent();
			Map<String, Object> response = new HashMap<>();
			response.put("employees", employees);
			response.put("currentPage", pageEmployees.getNumber());
			response.put("totalItems", pageEmployees.getTotalElements());
			response.put("totalPages", pageEmployees.getTotalPages());
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {
		
		Employee newEmployee = new Employee();
		newEmployee.setFirstName(employee.getFirstName());
		newEmployee.setLastName(employee.getLastName());
		newEmployee.setEmail(employee.getEmail());
		newEmployee.setAboutMe(employee.getAboutMe());
		newEmployee.setAlias(employee.getAlias());
		newEmployee.setCreateAt(new Date());
		return employeeRepository.save(newEmployee);
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {

		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));
		return ResponseEntity.ok(employee);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeReq) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

		employee.setFirstName(employeeReq.getFirstName());
		employee.setLastName(employeeReq.getLastName());
		employee.setEmail(employeeReq.getEmail());
		employee.setAboutMe(employeeReq.getAboutMe());
		employee.setAlias(employeeReq.getAlias());
		employee.setFgStatus(employeeReq.getFgStatus());
		
		Employee updateEmployee = employeeRepository.save(employee);

		return ResponseEntity.ok(updateEmployee);
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String, Boolean>> deteleEmployee(@PathVariable Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));
		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);

		return ResponseEntity.ok(response);
	}
}
