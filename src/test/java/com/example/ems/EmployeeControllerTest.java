package com.example.ems;

import com.example.ems.controller.EmployeeController;
import com.example.ems.dto.EmployeeData;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    private static final String END_POINT_PATH = "/api/employees";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmployeeService employeeService;

    @Test
    public void should_return_bad_request_if_createEmployee_has_invalid_request_body() throws Exception {
        // given
        EmployeeData newEmployee = new EmployeeData
                .EmployeeDataBuilder(1L, "", "", "")
                .build();

        String requestBody = objectMapper.writeValueAsString(newEmployee);

        // when
        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
                        .contentType("application/json")
                        .content(requestBody));
        // then
        response.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void should_return_ok_when_createEmployee_has_valid_request_body() throws Exception {
        // given
        EmployeeData newEmployee = new EmployeeData
                .EmployeeDataBuilder(1L, "John", "Doe", "John.Doe@gmail.com")
                .setAge(25)
                .setPhone("12345678910")
                .build();

        String requestBody = objectMapper.writeValueAsString(newEmployee);

        // when
        ResultActions response = mockMvc.perform(post(END_POINT_PATH)
                .contentType("application/json")
                .content(requestBody));

        // then
        response.andExpect(status().isCreated())
                .andDo(print());

        verify(employeeService, times(1)).createEmployee(refEq(newEmployee));
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void should_return_employee_by_id_from_getEmployeeById() throws Exception {
        // given
        EmployeeData newEmployee = new EmployeeData
                .EmployeeDataBuilder(1L, "John", "Doe", "John.Doe@gmail.com")
                .setAge(25)
                .build();

        when(employeeService.getEmployeeById(1L)).thenReturn(newEmployee);

        // when
        ResultActions response = mockMvc.perform(get(END_POINT_PATH + "/1")
                .contentType("application/json"));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("John.Doe@gmail.com"))
                .andExpect(jsonPath("$.age").value(25))
                .andDo(print());

        verify(employeeService, times(1)).getEmployeeById(1L);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void should_return_ResourceNotFoundException_from_getEmployeeById_when_employee_does_not_exist() throws Exception {
        // given
        long userId = 123L;
        String requestURI = END_POINT_PATH + "/" + userId;

        when(employeeService.getEmployeeById(userId)).thenThrow(ResourceNotFoundException.class);

        // then
        ResultActions response = mockMvc.perform(get(requestURI)
                        .contentType("application/json"));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void should_return_all_employees_from_getAllEmployees() throws Exception {
        // given
        EmployeeData newEmployee = new EmployeeData
                .EmployeeDataBuilder(1L, "John", "Doe", "John.Doe@gmail.com")
                .setAge(25)
                .build();

        EmployeeData newEmployee2 = new EmployeeData
                .EmployeeDataBuilder(2L, "Tom", "Cruise", "Tom.Cruise@gmail.com")
                .setPhone("12345678910")
                .build();

        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(newEmployee, newEmployee2));

        // when
        ResultActions response = mockMvc.perform(get(END_POINT_PATH)
                .contentType("application/json"));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("John.Doe@gmail.com"))
                .andExpect(jsonPath("$[0].age").value(25))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Tom"))
                .andExpect(jsonPath("$[1].lastName").value("Cruise"))
                .andExpect(jsonPath("$[1].email").value("Tom.Cruise@gmail.com"))
                .andExpect(jsonPath("$[1].phone").value("12345678910"))
                .andDo(print());

        verify(employeeService, times(1)).getAllEmployees();
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void should_return_ResourceNotFoundException_from_updateEmployee_when_employee_does_not_exist() throws Exception {
        // given
        long userId = 123L;
        String requestURI = END_POINT_PATH + "/" + userId;

        EmployeeData employeeToBeUpdated = new EmployeeData
                .EmployeeDataBuilder(123L, "John", "Doe", "John.Doe@gmail.com")
                .setAge(25)
                .setPhone("12345678910")
                .build();

        when(employeeService.updateEmployee(refEq(userId), refEq(employeeToBeUpdated))).thenThrow(ResourceNotFoundException.class);

        String requestBody = objectMapper.writeValueAsString(employeeToBeUpdated);

        // when
        ResultActions response = mockMvc.perform(put(requestURI)
                .contentType("application/json")
                .content(requestBody));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void should_return_ok_when_updateEmployee_has_valid_request_body() throws Exception {
        // given
        long userId = 1L;
        String requestURI = END_POINT_PATH + "/" + userId;

        EmployeeData employeeToBeUpdated = new EmployeeData
                .EmployeeDataBuilder(1L, "John", "Doe", "John.Doe@gmail.com")
                .setAge(25)
                .setPhone("12345678910")
                .build();

        when(employeeService.updateEmployee(refEq(1L), refEq(employeeToBeUpdated))).thenReturn(employeeToBeUpdated);

        String requestBody = objectMapper.writeValueAsString(employeeToBeUpdated);

        // when
        ResultActions response = mockMvc.perform(put(requestURI)
                    .contentType("application/json")
                    .content(requestBody));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("John.Doe@gmail.com"))
                .andExpect(jsonPath("$.age").value(25))
                .andDo(print());

        verify(employeeService, times(1)).updateEmployee(refEq(1L), refEq(employeeToBeUpdated));
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void should_return_ResourceNotFoundException_from_deleteEmployee_when_employee_does_not_exist() throws Exception {
        // given
        long userId = 123L;
        String requestURI = END_POINT_PATH + "/" + userId;

        doThrow(ResourceNotFoundException.class).when(employeeService).deleteEmployee(userId);

        // when
        ResultActions response = mockMvc.perform(delete(requestURI)
                .contentType("application/json"));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void should_return_ok_from_deleteEmployee() throws Exception {
        // given
        long userId = 123L;
        String requestURI = END_POINT_PATH + "/" + userId;

        // when
        ResultActions response = mockMvc.perform(delete(requestURI)
                .contentType("application/json"));
        
        // then
        response.andExpect(status().isOk())
                .andDo(print());

        verify(employeeService, times(1)).deleteEmployee(refEq(userId));
        verifyNoMoreInteractions(employeeService);
    }
}
