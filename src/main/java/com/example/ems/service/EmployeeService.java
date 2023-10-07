package com.example.ems.service;

import com.example.ems.dto.EmployeeData;

import java.util.List;

public interface EmployeeService {
    EmployeeData createEmployee(EmployeeData employeeData);

    EmployeeData getEmployeeById(Long employeeId);

    List<EmployeeData> getAllEmployees();

    EmployeeData updateEmployee(Long employeeId, EmployeeData updatedEmployee);

    void deleteEmployee(Long employeeId);
}
