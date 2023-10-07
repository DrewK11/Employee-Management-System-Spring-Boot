package com.example.ems.controller;

import com.example.ems.dto.EmployeeData;
import com.example.ems.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping(produces = {"application/json"}, consumes={"application/json"})
    public ResponseEntity<EmployeeData> createEmployee(@RequestBody @Valid EmployeeData employeeData) {
        EmployeeData savedEmployee = employeeService.createEmployee(employeeData);

        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeData> getEmployeeById(@PathVariable("id") Long employeeId) {
        EmployeeData employeeData = employeeService.getEmployeeById(employeeId);

        return ResponseEntity.ok(employeeData);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<EmployeeData>> getAllEmployees() {
        List<EmployeeData> employees = employeeService.getAllEmployees();

        return ResponseEntity.ok(employees);
    }

    @PutMapping("{id}")
    public ResponseEntity<EmployeeData> updateEmployee(@PathVariable("id") Long employeeID,
                                                       @RequestBody EmployeeData updatedEmployee) {
        EmployeeData employeeData = employeeService.updateEmployee(employeeID, updatedEmployee);
        return ResponseEntity.ok(employeeData);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
        employeeService.deleteEmployee(employeeId);

        return ResponseEntity.ok("Employee deleted successfully!");
    }
}
