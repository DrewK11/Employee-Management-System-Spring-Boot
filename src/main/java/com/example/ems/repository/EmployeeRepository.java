package com.example.ems.repository;

import com.example.ems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// when working with JPA, the repository MUST BE an interface that extends JpaRepository
// JpaRepository accepts 2 arguments <T, ID> where T is type of entity and ID is type of its primary key
// by extending the JpaRepo, the EmployeeRepo will get CRUD methods to affect the Employee entity
// by extending the JpaRepo, the EmployeeRepo will inherit all the CRUD methods
// we don't have to put the repository annotation here cause the JpaRepo has the implementation class SimpleJpaRepository inside that already
// has this annotation. It also has the transactional annotation, so all the methods are already transactional

// SimpleJpaRepository implements JpaRepository interface

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
