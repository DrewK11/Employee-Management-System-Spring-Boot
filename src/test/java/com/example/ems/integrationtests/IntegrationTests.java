package com.example.ems.integrationtests;

import com.example.ems.dto.EmployeeData;
import com.example.ems.entity.Employee;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static com.example.ems.TestUtil.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

// This annotation loads the application context, so you can bootstrap the web environment and port here
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    // This annotation helps us get the random port where our application starts when the tests run
    @LocalServerPort
    private int port;

    // We don't include the 8080 port below since our test will run on a random port
    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    // we need to inject our test repo here so we can verify that our employees are added to the test repo
    @Autowired
    private TestRepository repository;

    // This JUnit annotation means we will run this ONCE init before all test cases since we need it as setup
    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    // This annotation means we will run this before EACH test case (so multiple times)
    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/api/employees");
    }

    @Test
    public void should_return_EmployeeData_and_created_status_when_createEmployee() throws JSONException {
        // given
        String expectedResponse = "{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"John@gmail.com\",\"age\":20,\"phone\":null}";
        EmployeeData employee = new EmployeeData.EmployeeDataBuilder(1L, "John", "Doe", "John@gmail.com")
                .setAge(20)
                .build();

        // when
        // this restTemplate can call the rest controller from the test. That's how our integration test can work
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, employee, String.class);

        // then
        assertEquals(CREATED, response.getStatusCode());
        assertEquals(APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(1, repository.findAll().size());
        JSONAssert.assertEquals(expectedResponse, response.getBody(), true);
    }

    @Test
    @Sql(statements = "INSERT INTO EMPLOYEES (id, email_id, first_name, last_name, age, phone) VALUES (1, 'John@gmail.com', 'John', 'Doe', 20, '012345678910')",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM EMPLOYEES WHERE id = 1", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void should_return_all_employees_when_getAllEmployees() throws JSONException {
        // given
        String expectedResponse = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"John@gmail.com\",\"age\":20,\"phone\":012345678910}]";

        // when
        List<EmployeeData> response = restTemplate.getForObject(baseUrl, List.class);

        // then
        assertNotNull(response);
        assertEquals(1, repository.findAll().size());
        assertEquals(1, repository.findAll().size());
        JSONAssert.assertEquals(expectedResponse, response.toString(), true);
    }

    @Test
    @Sql(statements = "INSERT INTO EMPLOYEES (id, email_id, first_name, last_name, age, phone) VALUES (1, 'Tom@gmail.com', 'Tom', 'Cruise', 45, '012345678910')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM EMPLOYEES WHERE id = 1", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void should_return_employee_by_id_when_getEmployeeById() throws JSONException {
        // given
        String expectedResponse = "{\"id\":1,\"firstName\":\"Tom\",\"lastName\":\"Cruise\",\"email\":\"Tom@gmail.com\",\"age\":45,\"phone\":\"012345678910\"}";

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/{id}", String.class, 1);

        // then
        assertNotNull(response.getBody());
        assertEquals(OK, response.getStatusCode());
        assertEquals(APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(1, repository.findAll().size());
        JSONAssert.assertEquals(expectedResponse, response.getBody(), true);
    }

    @Test
    @Sql(statements = "INSERT INTO EMPLOYEES (id, email_id, first_name, last_name, age, phone) VALUES (1, 'Tom@gmail.com', 'Tom', 'Cruise', 45, '012345678910')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM EMPLOYEES WHERE id = 1", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void should_update_employee_by_id_when_updateEmployee() throws JSONException {
        // given
        EmployeeData employeeDataToBeUpdated = new EmployeeData.EmployeeDataBuilder(1L, "Tommy", "Cruise", "Tommy@gmail.com")
                .setAge(50)
                .build();

        // when
        restTemplate.put(baseUrl + "/{id}", employeeDataToBeUpdated, 1);
        Optional<Employee> updatedEmployee = repository.findById(1L);

        // then
        assertEquals("Tommy", updatedEmployee.orElseThrow().getFirstName());
        assertEquals("Cruise", updatedEmployee.orElseThrow().getLastName());
        assertEquals(50, updatedEmployee.orElseThrow().getAge());
        assertEquals("Tommy@gmail.com", updatedEmployee.orElseThrow().getEmail());
    }

    @Test
    @Sql(statements = "INSERT INTO EMPLOYEES (id, email_id, first_name, last_name, age, phone) VALUES (1, 'Tom@gmail.com', 'Tom', 'Cruise', 45, '012345678910')",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void should_delete_employee_by_id_when_deleteEmployee() throws JSONException {
        // given
        int recordCountBeforeDeletion = repository.findAll().size();
        assertEquals(1, recordCountBeforeDeletion);

        // when
        restTemplate.delete(baseUrl + "/{id}", 1);

        // then
        int recordCountAfterDeletion = repository.findAll().size();
        assertEquals(0, recordCountAfterDeletion);
    }
}
