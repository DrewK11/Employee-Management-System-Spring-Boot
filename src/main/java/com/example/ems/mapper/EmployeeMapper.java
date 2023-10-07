package com.example.ems.mapper;

import com.example.ems.dto.EmployeeData;
import com.example.ems.entity.Employee;

public class EmployeeMapper {

    public static EmployeeData mapToEmployeeData(Employee employee) {
        return new EmployeeData.EmployeeDataBuilder(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail())
                .setAge(employee.getAge())
                .setPhone(employee.getPhone())
                .build();
    }

    public static Employee mapToEmployee(EmployeeData employeeData) {
        return new Employee(
                employeeData.getId(),
                employeeData.getFirstName(),
                employeeData.getLastName(),
                employeeData.getEmail(),
                employeeData.getAge(),
                employeeData.getPhone()
        );
    }
}
