package com.example.ems.integrationtests;

import com.example.ems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Employee, Long> {
}
