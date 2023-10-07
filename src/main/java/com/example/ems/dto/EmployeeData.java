package com.example.ems.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@JsonDeserialize(builder = EmployeeData.EmployeeDataBuilder.class)
public class EmployeeData {
    private final Long id;
    @Length(min = 3, max = 20)
    private final String firstName;
    @Length(min = 2, max = 20)
    private final String lastName;
    @NotBlank(message = "Email must not be empty")
    @Email(message = "User must have a valid email")
    @Length(min = 3, max = 50)
    private final String email;
    private final Integer age;
    private final String phone;

    private EmployeeData(EmployeeDataBuilder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.age = builder.age;
        this.phone = builder.phone;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
    public static class EmployeeDataBuilder {
        private final Long id;
        private final String firstName;
        private final String lastName;
        private final String email;
        private Integer age;
        private String phone;

        public EmployeeDataBuilder(Long id, String firstName, String lastName, String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        public EmployeeDataBuilder setAge(Integer age) {
            this.age = age;
            return this;
        }

        public EmployeeDataBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public EmployeeData build() {
            return new EmployeeData(this);
        }
    }
}
