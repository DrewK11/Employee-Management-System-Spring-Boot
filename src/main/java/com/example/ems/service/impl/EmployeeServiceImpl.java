package com.example.ems.service.impl;

import com.example.ems.dto.EmployeeData;
import com.example.ems.entity.Employee;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.mapper.EmployeeMapper;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeData createEmployee(EmployeeData employeeData) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeData);
        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeData(savedEmployee);
    }

    @Override
    public EmployeeData getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exist with the given id: " + employeeId));

        return EmployeeMapper.mapToEmployeeData(employee);
    }

    @Override
    public List<EmployeeData> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(EmployeeMapper::mapToEmployeeData)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeData updateEmployee(Long employeeId, EmployeeData updatedEmployee) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exist with the given id: " + employeeId));

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        employee.setAge(updatedEmployee.getAge());
        employee.setPhone(updatedEmployee.getPhone());

        Employee updatedEmployeeObj = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeData(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        // the findById method returns an Optional employee. If it doesn't exist then we throw our custom exception
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exist with the given id: " + employeeId));

        employeeRepository.deleteById(employeeId);
    }
}
