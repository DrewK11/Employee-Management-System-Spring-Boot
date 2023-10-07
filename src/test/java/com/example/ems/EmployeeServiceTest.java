package com.example.ems;

import com.example.ems.dto.EmployeeData;
import com.example.ems.entity.Employee;
import com.example.ems.repository.EmployeeRepository;
import com.example.ems.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    EmployeeServiceImpl service;

    @Mock
    EmployeeRepository repository;

    @Test
    public void getEmployeeById_should_return_correct_employee() {
        // given
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Dan")
                .lastName("Smith")
                .email("Dan@gmail.com")
                .build();

        // when
        when(repository.findById(employee.getId())).thenReturn(Optional.of(employee));
        EmployeeData savedEmployee = service.getEmployeeById(employee.getId());

        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("Dan");
        assertThat(savedEmployee.getLastName()).isEqualTo("Smith");
        assertThat(savedEmployee.getEmail()).isEqualTo("Dan@gmail.com");
    }

    @Test
    public void getAllEmployees_should_return_all_employees() {
        // given
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Dan")
                .lastName("Smith")
                .email("Dan@gmail.com")
                .build();
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .email("John@gmail.com")
                .build();

        // when
        when(repository.findAll()).thenReturn(List.of(employee1, employee2));
        List<EmployeeData> employees = service.getAllEmployees();

        // then
        assertThat(employees).size().isEqualTo(2);
        assertThat(employees.get(0).getFirstName()).isEqualTo("Dan");
        assertThat(employees.get(0).getLastName()).isEqualTo("Smith");
        assertThat(employees.get(0).getEmail()).isEqualTo("Dan@gmail.com");

        assertThat(employees.get(1).getFirstName()).isEqualTo("John");
        assertThat(employees.get(1).getLastName()).isEqualTo("Doe");
        assertThat(employees.get(1).getEmail()).isEqualTo("John@gmail.com");
    }

    @Test
    public void updateEmployee_should_return_update_correct_employee() {
        // given
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Dan")
                .lastName("Smith")
                .email("Dan@gmail.com")
                .age(20)
                .build();

        EmployeeData employeeData = new EmployeeData
                .EmployeeDataBuilder(1L, "John", "Doe", "John@gmail.com")
                .setAge(25)
                .build();

        // when
        when(repository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(repository.save(employee)).thenReturn(employee);
        EmployeeData savedEmployee = service.updateEmployee(employee.getId(), employeeData);

        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("John");
        assertThat(savedEmployee.getLastName()).isEqualTo("Doe");
        assertThat(savedEmployee.getEmail()).isEqualTo("John@gmail.com");
    }

    @Test
    public void deleteEmployee_should_return_delete_employee() {
        // given
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Dan")
                .lastName("Smith")
                .email("Dan@gmail.com")
                .build();

        // when
        when(repository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(repository).deleteById(1L);

        // then
        assertAll(() -> service.deleteEmployee(employee.getId()));
    }
}
